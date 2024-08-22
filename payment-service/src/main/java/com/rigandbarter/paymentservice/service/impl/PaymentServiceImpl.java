package com.rigandbarter.paymentservice.service.impl;

import com.rigandbarter.core.models.UserBasicInfo;
import com.rigandbarter.core.models.UserBillingInfo;
import com.rigandbarter.paymentservice.dto.StripeProductRequest;
import com.rigandbarter.paymentservice.model.StripeCustomer;
import com.rigandbarter.paymentservice.model.StripeProduct;
import com.rigandbarter.paymentservice.repository.IStripeCustomerRepository;
import com.rigandbarter.paymentservice.repository.IStripeProductRepository;
import com.rigandbarter.paymentservice.service.IPaymentService;
import com.stripe.Stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.StripeResponse;
import com.stripe.param.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
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
        TODO: Flow 2 (USE THIS ONE)
            1) When user enters billing info, it creates a Stripe customer and payment method (save them locally too)
            2) When the buyer and seller accept, create a setupIntent (or paymentIntent...not sure which one)
            3) Once the transaction is completed, complete the payment
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
    public StripeCustomer updatedStripeCustomerPaymentInfo(String userId, UserBillingInfo billingInfo) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        StripeCustomer customer = this.stripeCustomerRepository.findByUserId(userId);
        if(customer == null)
            throw new RuntimeException("Failed to updated stripe customer payment info for user: " + userId + ". User not found");

        /**
         * TODO: NEW billing info flow
         *  1) See https://docs.stripe.com/payments/payment-element
         *  2) Capture their billing info on with the form from the doco, set it to the stipe customer
         *  3) Modify billingInfo db table to capture necessary info (may not need it)
         *  4) See if can use that below instead of TEST_CARD_TOKEN
         */

        // Format month and year to longs
//        String[] monthYearExpiration = billingInfo.getExpirationDate().split("/");
//        Long month = Long.valueOf(monthYearExpiration[0]);
//
//        String yearStr = monthYearExpiration[1].length() == 2 ? "20"+monthYearExpiration[1] : monthYearExpiration[1];
//        Long year = Long.valueOf(yearStr);

        PaymentMethodCreateParams params =
                PaymentMethodCreateParams.builder()
                        .setType(PaymentMethodCreateParams.Type.CARD)
                        .setCard(PaymentMethodCreateParams.Token.builder().setToken(TEST_CARD_TOKEN).build())
// TODO: Get this info from stripe front end integration maybe??
//                        .setCard(
//                                PaymentMethodCreateParams.CardDetails.builder()
//                                        .setNumber(billingInfo.getCardNumber())
//                                        .setExpMonth(month)
//                                        .setExpYear(year)
//                                        .setCvc(billingInfo.getCvv())
//                                        .build()
//                        )
                        .build();

        PaymentMethod paymentMethod = PaymentMethod.create(params);

        // Attach payment to the customer
        PaymentMethodAttachParams paymentMethodAttachParams =
                PaymentMethodAttachParams.builder().setCustomer(customer.getStripeId()).build();

        paymentMethod = paymentMethod.attach(paymentMethodAttachParams);

        // Update StripeCustomer to have billing info
        customer.setPaymentId(paymentMethod.getId());
        customer.setCardLast4(paymentMethod.getCard().getLast4());
        customer.setExpirationMonth(paymentMethod.getCard().getExpMonth());
        customer.setExpirationYear(paymentMethod.getCard().getExpYear());
        customer.setCardCvv(billingInfo.getCvv());

        stripeCustomerRepository.save(customer);

        return null;
    }
}
