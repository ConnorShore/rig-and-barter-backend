package com.rigandbarter.paymentservice.service.impl;

import com.rigandbarter.core.models.UserBasicInfo;
import com.rigandbarter.core.models.UserBillingInfo;
import com.rigandbarter.paymentservice.dto.StripeCustomerInfoResponse;
import com.rigandbarter.paymentservice.dto.StripeProductRequest;
import com.rigandbarter.paymentservice.model.StripeCustomer;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements IPaymentService {

    private final String stripeSecretKey;
    private final String USD_CURRENCY = "usd";
    private final String TEST_CARD_TOKEN = "tok_visa";

    private final IStripeProductRepository stripeProductRepository;
    private final IStripeCustomerRepository stripeCustomerRepository;

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
    public StripeCustomer updatedStripeCustomerPaymentInfo(String userId, UserBillingInfo billingInfo) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        StripeCustomer customer = this.stripeCustomerRepository.findByUserId(userId);
        if(customer == null)
            throw new RuntimeException("Failed to updated stripe customer payment info for user: " + userId + ". User not found");

        PaymentMethodCreateParams params =
                PaymentMethodCreateParams.builder()
                        .setType(PaymentMethodCreateParams.Type.CARD)
                        .setCard(PaymentMethodCreateParams.Token.builder().setToken(billingInfo.getStripeCardToken()).build())
                        .build();

        PaymentMethod paymentMethod = PaymentMethod.create(params);

        // Attach payment to the customer
        PaymentMethodAttachParams paymentMethodAttachParams =
                PaymentMethodAttachParams.builder().setCustomer(customer.getStripeId()).build();

        paymentMethod = paymentMethod.attach(paymentMethodAttachParams);

        // Update StripeCustomer to have billing info
        customer.setPaymentId(paymentMethod.getId());

        stripeCustomerRepository.save(customer);

        return null;
    }

    @Override
    public StripeCustomerInfoResponse getStripeCustomerInfo(String userId) {
        StripeCustomer stripeCustomer = stripeCustomerRepository.findByUserId(userId);

        return StripeCustomerInfoResponse.builder()
                .userId(userId)
                .stripeId(stripeCustomer.getStripeId())
                .paymentId(stripeCustomer.getPaymentId())
                .accountId(stripeCustomer.getAccountId())
                .build();
    }

    @Override
    public void deleteStripeAccount(String accountId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        Account resource = Account.retrieve(accountId);
        resource.delete();
    }
}
