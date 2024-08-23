package com.rigandbarter.paymentservice.service.impl;

import com.rigandbarter.core.models.UserBasicInfo;
import com.rigandbarter.eventlibrary.components.RBEventProducer;
import com.rigandbarter.eventlibrary.components.RBEventProducerFactory;
import com.rigandbarter.eventlibrary.events.StripeCustomerCreatedEvent;
import com.rigandbarter.paymentservice.dto.StripePaymentMethodRequest;
import com.rigandbarter.core.models.StripePaymentMethodResponse;
import com.rigandbarter.core.models.StripeCustomerResponse;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PaymentServiceImpl implements IPaymentService {

    private final String stripeSecretKey;
    private final String USD_CURRENCY = "usd";
    private final String TEST_CARD_TOKEN = "tok_visa";

    private final RBEventProducer stripeCustomerCreatedProducer;

    private final IStripeProductRepository stripeProductRepository;
    private final IStripeCustomerRepository stripeCustomerRepository;

    public PaymentServiceImpl(String stripeSecretKey,
                              IStripeProductRepository stripeProductRepository,
                              IStripeCustomerRepository stripeCustomerRepository,
                              RBEventProducerFactory rbEventProducerFactory) {
        this.stripeSecretKey = stripeSecretKey;
        this.stripeProductRepository = stripeProductRepository;
        this.stripeCustomerRepository = stripeCustomerRepository;
        this.stripeCustomerCreatedProducer = rbEventProducerFactory.createProducer(StripeCustomerCreatedEvent.class);
    }

    /*
        TODO:
            1) MAY WANT TO DO THIS: https://docs.stripe.com/connect/collect-then-transfer-guide?platform=web instead of direct payments
                -Create account when create user
            DONE) When user enters billing info, it creates a Stripe customer and payment method (save them locally too)
            2) When the buyer and seller accept, create a setupIntent (or paymentIntent...not sure which one) (TransactionInProgressEvent)
            3) Once the transaction is completed, complete the payment (TransactionCompletedEvent)

     */

    @Override
    public String createStripeProduct(StripeProductRequest stripeProductRequest) throws StripeException {
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
                        .setProduct(product.getId())
                        .setCurrency(USD_CURRENCY)
                        .setUnitAmount(unitPriceInCents)
                        .build();

        Price price = Price.create(params);
        System.out.println("Success! Here is your price id: " + price.getId());

        // Save the product to the database
        StripeProduct stripeProduct = StripeProduct.builder()
                .stripeId(product.getId())
                .userId(stripeProductRequest.getUserId())
                .name(stripeProductRequest.getName())
                .description(stripeProductRequest.getDescription())
                .currency(USD_CURRENCY)
                .priceInCents(unitPriceInCents)
                .build();

        stripeProductRepository.save(stripeProduct);

        return product.getId();
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
            AccountLinkCreateParams linkParams =
                    AccountLinkCreateParams.builder()
                            .setAccount(account.getId())
                            .setRefreshUrl("http://localhost:8080/api/payment/reauth")
                            .setReturnUrl("http://localhost:4200")
                            .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                            .build();

            AccountLink accountLink = AccountLink.create(linkParams);
            redirectUrl = accountLink.getUrl();
            accountId = account.getId();
        } catch (Exception e) {
            account.delete();
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
    public void deleteStripeAccount(String accountId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        Account resource = Account.retrieve(accountId);
        resource.delete();
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
                .nameOnCard(paymentMethodRequest.getNameOnCard())
                .expirationMonth(paymentMethod.getCard().getExpMonth())
                .expirationYear(paymentMethod.getCard().getExpYear())
                .last4Digits(paymentMethod.getCard().getLast4())
                .build();

        List<StripePaymentMethod> currentPaymentMethods = customer.getPaymentMethods();
        currentPaymentMethods.add(newPaymentMethod);
        customer.setPaymentMethods(currentPaymentMethods);

        stripeCustomerRepository.save(customer);

        return StripePaymentMethodResponse.builder()
                .stripePaymentId(newPaymentMethod.getStripePaymentId())
                .nameOnCard(newPaymentMethod.getNameOnCard())
                .last4Digits(newPaymentMethod.getLast4Digits())
                .expirationMonth(newPaymentMethod.getExpirationMonth())
                .expirationYear(newPaymentMethod.getExpirationYear())
                .cardToken(newPaymentMethod.getCardToken())
                .build();
    }
}
