package com.rigandbarter.paymentservice.service;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.core.models.UserBasicInfo;
import com.rigandbarter.core.models.UserBillingInfo;
import com.rigandbarter.paymentservice.dto.StripeProductRequest;
import com.rigandbarter.paymentservice.model.StripeCustomer;
import com.stripe.exception.StripeException;
import com.stripe.net.StripeResponse;

public interface IPaymentService {

    /**
     * Creates a product in Stripe and our db
     * @param stripeProductRequest The details of the product
     * @return The Stripe id of the product
     * @throws StripeException Fails to create product/price in Stripe
     */
    String createStripeProduct(StripeProductRequest stripeProductRequest) throws StripeException;

    /**
     * Creates a customer in stripe and our db
     * @param basicInfo The info of the customer to create
     * @return The created customer
     * @throws StripeException Fails to create the user in stripe
     */
    StripeCustomer createStripeCustomer(UserBasicInfo basicInfo) throws StripeException;

    /**
     * Sets the payment info for the stripe customer and in our db
     * @param userId The id of the user to set billing info for
     * @param billingInfo The billing info to set
     * @return The updated stripe customer
     * @throws StripeException Fails to create stripe payment info
     */
    StripeCustomer updatedStripeCustomerPaymentInfo(String userId, UserBillingInfo billingInfo) throws StripeException;
}
