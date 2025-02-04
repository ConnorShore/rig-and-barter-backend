package com.rigandbarter.paymentservice.service.impl;

import com.rigandbarter.core.models.ListingResponse;
import com.rigandbarter.core.models.UserBasicInfo;
import com.rigandbarter.eventlibrary.components.RBEventProducer;
import com.rigandbarter.eventlibrary.components.RBEventProducerFactory;
import com.rigandbarter.eventlibrary.events.StripeCustomerCreatedEvent;
import com.rigandbarter.eventlibrary.events.TransactionCompletedEvent;
import com.rigandbarter.eventlibrary.events.UserVerifyEvent;
import com.rigandbarter.paymentservice.client.ListingServiceClient;
import com.rigandbarter.paymentservice.dto.StripePaymentMethodRequest;
import com.rigandbarter.core.models.StripePaymentMethodResponse;
import com.rigandbarter.core.models.StripeCustomerResponse;
import com.rigandbarter.core.models.StripeProductCreationResponse;
import com.rigandbarter.paymentservice.dto.StripeProductRequest;
import com.rigandbarter.paymentservice.mapper.StripeMapper;
import com.rigandbarter.paymentservice.model.StripeCustomer;
import com.rigandbarter.paymentservice.model.StripePaymentMethod;
import com.rigandbarter.paymentservice.model.StripeProduct;
import com.rigandbarter.paymentservice.repository.IStripeCustomerRepository;
import com.rigandbarter.paymentservice.repository.IStripeProductRepository;
import com.rigandbarter.paymentservice.service.IPaymentService;
import com.stripe.Stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentServiceImpl implements IPaymentService {

    @Value("${rb.front-end.url}")
    private String FRONT_END_URL;

    @Value("${server.port}")
    private String SERVER_PORT;

    private final String EVENT_SOURCE = "PaymentService";
    private final String stripeSecretKey;
    private final String stripeFeePercent;
    private final String USD_CURRENCY = "usd";
    private final String TEST_CARD_TOKEN = "tok_visa";

    private final RBEventProducer stripeCustomerCreatedProducer;
    private final RBEventProducer userVerifyProducer;

    private final IStripeProductRepository stripeProductRepository;
    private final IStripeCustomerRepository stripeCustomerRepository;

    private final ListingServiceClient listingServiceClient;

    public PaymentServiceImpl(String stripeSecretKey, String stripeFeePercent,
                              IStripeProductRepository stripeProductRepository,
                              IStripeCustomerRepository stripeCustomerRepository,
                              RBEventProducerFactory rbEventProducerFactory,
                              ListingServiceClient listingServiceClient) {
        this.stripeSecretKey = stripeSecretKey;
        this.stripeFeePercent = stripeFeePercent;
        this.stripeProductRepository = stripeProductRepository;
        this.stripeCustomerRepository = stripeCustomerRepository;
        this.stripeCustomerCreatedProducer = rbEventProducerFactory.createProducer(StripeCustomerCreatedEvent.class);
        this.userVerifyProducer = rbEventProducerFactory.createProducer(UserVerifyEvent.class);
        this.listingServiceClient = listingServiceClient;
    }

    @Override
    public StripeProductCreationResponse createStripeProduct(StripeProductRequest stripeProductRequest) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        // Save the product to stripe
        ProductCreateParams productParams =
                ProductCreateParams.builder()
                        .setName(stripeProductRequest.getName())
                        .setDescription(stripeProductRequest.getDescription())
                        .build();
        Product product = Product.create(productParams);
        System.out.println("Success! Here is your product id: " + product.getId());

        Long unitPriceInCents = (long) (stripeProductRequest.getProductPrice() * 100);
        PriceCreateParams params =
                PriceCreateParams
                        .builder()
                        .setActive(true)
                        .setProduct(product.getId())
                        .setCurrency(USD_CURRENCY)
                        .setUnitAmount(unitPriceInCents)
                        .build();

        Price price = Price.create(params);
        System.out.println("Success! Here is your price id: " + price.getId());

        // Save the product to the database
        StripeProduct stripeProduct = StripeProduct.builder()
                .stripeProductId(product.getId())
                .stripePriceId(price.getId())
                .userId(stripeProductRequest.getUserId())
                .name(stripeProductRequest.getName())
                .description(stripeProductRequest.getDescription())
                .currency(USD_CURRENCY)
                .priceInCents(unitPriceInCents)
                .build();

        stripeProductRepository.save(stripeProduct);

        return StripeProductCreationResponse.builder()
                .stripeProductId(product.getId())
                .stripePriceId(price.getId())
                .build();
    }

    @Override
    public void updateStripeProductPrice(String productId, double price) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        StripeProduct stripeProduct = stripeProductRepository.findByStripeProductId(productId);
        if(stripeProduct == null)
            throw new NotFoundException("Product with id: " + productId + " not found");

        Product product = Product.retrieve(productId);
        if(product == null)
            throw new NotFoundException("Product with id: " + productId + " not found in Stripe.");

        Price productPrice = Price.retrieve(stripeProduct.getStripePriceId());
        if(productPrice == null)
            throw new NotFoundException("Price with id: " + stripeProduct.getStripePriceId() + " not found in Stripe.");

        // Set the price to inactive and delete it
        productPrice.setActive(false);
        productPrice.setDeleted(true);

        // Create a new price
        Long unitPriceInCents = (long) (price * 100);
        PriceCreateParams params =
                PriceCreateParams
                        .builder()
                        .setActive(true)
                        .setProduct(product.getId())
                        .setCurrency(USD_CURRENCY)
                        .setUnitAmount(unitPriceInCents)
                        .build();

        Price newPrice = Price.create(params);

        stripeProduct.setStripePriceId(newPrice.getId());
        stripeProduct.setPriceInCents(unitPriceInCents);
        stripeProductRepository.save(stripeProduct);
    }

    @Override
    public StripeCustomer createStripeCustomer(UserBasicInfo basicInfo) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        CustomerCreateParams params =
                CustomerCreateParams.builder()
                        .setName(basicInfo.getFirstName() + " " + basicInfo.getLastName())
                        .setEmail(basicInfo.getEmail())
                        .build();

        Customer customer = Customer.create(params);

        StripeCustomer stripeCustomer = StripeCustomer.builder()
                .userId(basicInfo.getUserId())
                .stripeId(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .build();

        stripeCustomerRepository.save(stripeCustomer);

        // Create event to update user object with stripe customer
        StripeCustomerCreatedEvent stripeCustomerCreatedEvent = StripeCustomerCreatedEvent.builder()
                .id(UUID.randomUUID().toString())
                .source(EVENT_SOURCE)
                .creationDate(LocalDateTime.now())
                .userId(stripeCustomer.getUserId())
                .stripeCustomerId(stripeCustomer.getStripeId())
                .build();
        stripeCustomerCreatedProducer.send(stripeCustomerCreatedEvent);

        return stripeCustomer;
    }

    @Override
    public String createStripeCustomerAccount(String userId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        StripeCustomer stripeCustomer = stripeCustomerRepository.findByUserId(userId);

        // Get first and last name from the full name
        String[] fullName = stripeCustomer.getName().split(" ");
        String firstName = fullName[0];
        String lastName = fullName.length > 1 ? fullName[1] : "";

        AccountCreateParams params =
            AccountCreateParams.builder()
                .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
                .setType(AccountCreateParams.Type.EXPRESS)
                .setEmail(stripeCustomer.getEmail())
                    .setIndividual(AccountCreateParams.Individual.builder()
                            .setEmail(stripeCustomer.getEmail())
                            .setFirstName(firstName)
                            .setLastName(lastName)
                            .build()
                    )
                .build();

        Account account = Account.create(params);

        String accountId = null;
        String redirectUrl = null;
        try {
            String refreshUrl = String.format("http://localhost:%s/api/payment/reauth", SERVER_PORT);
            AccountLinkCreateParams linkParams =
                    AccountLinkCreateParams.builder()
                            .setAccount(account.getId())
                            .setRefreshUrl(refreshUrl)
                            .setReturnUrl(FRONT_END_URL)
                            .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                            .build();

            AccountLink accountLink = AccountLink.create(linkParams);
            redirectUrl = accountLink.getUrl();
            accountId = account.getId();
        } catch (Exception e) {
            account.delete();
        }

        if(!stripeCustomer.getPaymentMethods().isEmpty() && !stripeCustomer.isVerified()) {
            stripeCustomer.setVerified(true);
            userVerifyProducer.send(createUserVerifyEvent(stripeCustomer.getUserId(), true));
        }

        stripeCustomer.setAccountId(accountId);
        stripeCustomerRepository.save(stripeCustomer);

        return redirectUrl;
    }

    @Override
    public StripeCustomerResponse getStripeCustomerInfo(String userId) {
        StripeCustomer stripeCustomer = stripeCustomerRepository.findByUserId(userId);
        return StripeMapper.customerEntityToDto(stripeCustomer);
    }

    @Override
    public void deleteStripeAccount(String accountId, String userId, boolean isTest) throws AuthenticationException {
        Stripe.apiKey = stripeSecretKey;

        StripeCustomer customer = stripeCustomerRepository.findByUserId(userId);

        if(!isTest && !customer.getAccountId().equals(accountId))
            throw new AuthenticationException("User[" + userId + "] does not have access to account: " + accountId);

        try {
            Account resource = Account.retrieve(accountId);
            resource.delete();
        } catch (StripeException e) {
            // Fail silently
        }

        customer.setAccountId(null);

        if(customer.isVerified()) {
            customer.setVerified(false);
            userVerifyProducer.send(createUserVerifyEvent(customer.getUserId(), false));
        }

        stripeCustomerRepository.save(customer);
    }

    @Override
    public StripePaymentMethodResponse addPaymentMethod(String userId, StripePaymentMethodRequest paymentMethodRequest) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        StripeCustomer customer = this.stripeCustomerRepository.findByUserId(userId);
        if(customer == null)
            throw new RuntimeException("Failed to updated stripe customer payment info for user: " + userId + ". User not found");

        PaymentMethodCreateParams params =
                PaymentMethodCreateParams.builder()
                        .setType(PaymentMethodCreateParams.Type.CARD)
                        .setCard(PaymentMethodCreateParams.Token.builder().setToken(paymentMethodRequest.getCardToken()).build())
                        .build();

        PaymentMethod paymentMethod = PaymentMethod.create(params);

        // Attach payment to the customer
        PaymentMethodAttachParams paymentMethodAttachParams =
                PaymentMethodAttachParams.builder().setCustomer(customer.getStripeId()).build();

        paymentMethod = paymentMethod.attach(paymentMethodAttachParams);

        // Update StripeCustomer to have billing info
        StripePaymentMethod newPaymentMethod = StripePaymentMethod.builder()
                .userId(userId)
                .stripePaymentId(paymentMethod.getId())
                .cardToken(paymentMethodRequest.getCardToken())
                .nickname(paymentMethodRequest.getNickname())
                .expirationMonth(paymentMethod.getCard().getExpMonth())
                .expirationYear(paymentMethod.getCard().getExpYear())
                .last4Digits(paymentMethod.getCard().getLast4())
                .build();

        List<StripePaymentMethod> currentPaymentMethods = customer.getPaymentMethods();
        currentPaymentMethods.add(newPaymentMethod);
        customer.setPaymentMethods(currentPaymentMethods);

        if(customer.getAccountId() != null && !customer.isVerified()) {
            customer.setVerified(true);
            userVerifyProducer.send(createUserVerifyEvent(customer.getUserId(), true));
        }

        stripeCustomerRepository.save(customer);

        return StripePaymentMethodResponse.builder()
                .stripePaymentId(newPaymentMethod.getStripePaymentId())
                .nickname(newPaymentMethod.getNickname())
                .last4Digits(newPaymentMethod.getLast4Digits())
                .expirationMonth(newPaymentMethod.getExpirationMonth())
                .expirationYear(newPaymentMethod.getExpirationYear())
                .cardToken(newPaymentMethod.getCardToken())
                .build();
    }

    @Override
    public void deletePaymentMethod(String paymentId, String userId) throws AuthenticationException {
        Stripe.apiKey = stripeSecretKey;

        StripeCustomer customer = stripeCustomerRepository.findByUserId(userId);

        boolean userContainsPayment = customer.getPaymentMethods().stream()
                        .anyMatch(pm -> pm.getStripePaymentId().equals(paymentId));
        if(!userContainsPayment)
            throw new AuthenticationException("User[" + userId + "] does not have access to payment: " + paymentId);

        customer.setPaymentMethods(
                customer.getPaymentMethods().stream()
                    .filter(pm -> !pm.getStripePaymentId().equals(paymentId))
                    .collect(Collectors.toList()
                )
        );

        if(customer.getPaymentMethods().isEmpty() && customer.isVerified()) {
            customer.setVerified(false);
            userVerifyProducer.send(createUserVerifyEvent(customer.getUserId(), false));
        }

        try {
            PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentId);
            paymentMethod.detach();
        } catch (StripeException e) {
            // Fail silently
        }

        stripeCustomerRepository.save(customer);
    }

    @Override
    public void completeTransaction(TransactionCompletedEvent transactionCompletedEvent) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        StripeCustomer buyerCustomer = stripeCustomerRepository.findByUserId(transactionCompletedEvent.getBuyerId());
        StripeCustomer sellerCustomer = stripeCustomerRepository.findByUserId(transactionCompletedEvent.getSellerId());

        if(buyerCustomer == null)
            throw new NotFoundException("Error: The buyer user is not found. Failed to move transaction to In Progress.");
        if(sellerCustomer == null)
            throw new NotFoundException("Error: The seller user is not found. Failed to move transaction to In Progress.");

        if(!buyerCustomer.isVerified() || !sellerCustomer.isVerified()) {
            log.info("No transaction setup intent created as buyer and seller weren't both verified");
            throw new NotAuthorizedException("Cannot complete secured transaction if buyer and seller aren't both verified");
        }

        ListingResponse listing = listingServiceClient.getListing(transactionCompletedEvent.getListingId(), "Bearer " + transactionCompletedEvent.getAuthToken());
        if(listing == null)
            throw new NotFoundException("Listing item for the transaction does not exist. Cancelling transaction.");

        completePayment(listing, transactionCompletedEvent.getPaymentMethodId(),
                buyerCustomer.getStripeId(), sellerCustomer.getAccountId());
    }

    /**
     * Complete the payment for the listing.
     * @param listing The listing to complete the payment for
     * @param paymentMethodId The payment method of the buyer that is being used
     * @param buyerStripeId The id of the buyer in stripe
     * @param sellerAccountId The seller's connected account id
     * @throws StripeException If fails to create/complete payment
     */
    private void completePayment(ListingResponse listing, String paymentMethodId, String buyerStripeId, String sellerAccountId) throws StripeException {
        long payout = (long)(listing.getPrice() * 100);
        long fee = (long)((listing.getPrice() * Double.parseDouble(stripeFeePercent)) * 100);

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(payout)
                        .setCurrency("usd")
                        .setCustomer(buyerStripeId)
                        .setPaymentMethod(paymentMethodId)
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                        .build()
                        )
                        .setApplicationFeeAmount(fee)
                        .setTransferData(
                                PaymentIntentCreateParams.TransferData.builder()
                                        .setDestination(sellerAccountId)
                                        .build()
                        )
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        paymentIntent = paymentIntent.confirm();

        switch (paymentIntent.getStatus()) {
            case "succeeded":
                // TODO: Send notification to buyer and seller that transaction is complete
                // TODO: Delete listing once transaction is completed
                System.out.println();
                break;
            case "processing":
                System.out.println();
                break;
            case "requires_payment_method":
                System.out.println();
                break;
            default:
                System.out.println();
                break;
        }
    }

    /**
     * Helper to construct a user verify event
     * @param userId The id of the user
     * @param isVerified True if the user is verified, false otherwise
     * @return The created user verify event
     */
    private UserVerifyEvent createUserVerifyEvent(String userId, boolean isVerified) {
        return UserVerifyEvent.builder()
                .id(UUID.randomUUID().toString())
                .source(EVENT_SOURCE)
                .creationDate(LocalDateTime.now())
                .userId(userId)
                .isVerified(isVerified)
                .build();
    }
}
