package com.rigandbarter.paymentservice.service;

import com.rigandbarter.core.models.UserBasicInfo;
import com.rigandbarter.core.models.UserBillingInfo;
import com.rigandbarter.paymentservice.dto.StripeCustomerInfoResponse;
import com.rigandbarter.paymentservice.dto.StripeProductRequest;
import com.rigandbarter.paymentservice.model.StripeCustomer;
import com.stripe.exception.StripeException;

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
     * Creates a connected account for the user to recieve money
     * @param userId The id for the user to create the account for
     * @return The redirect url for the customer to etner account info
     */
    String createStripeCustomerAccount(String userId) throws StripeException;

    /**
     * Sets the payment info for the stripe customer and in our db
     * @param userId The id of the user to set billing info for
     * @param billingInfo The billing info to set
     * @return The updated stripe customer
     * @throws StripeException Fails to create stripe payment info
     */
    StripeCustomer updatedStripeCustomerPaymentInfo(String userId, UserBillingInfo billingInfo) throws StripeException;

    /**
     * Gets the user's stripe information
     * @param userId The id of the user to get the stripe info for
     * @return The stripe information
     */
    StripeCustomerInfoResponse getStripeCustomerInfo(String userId);

    /**
     * Deletes an account with the specified ID
     * @param accountId The id of the account to delete
     * @throws StripeException
     */
    void deleteStripeAccount(String accountId) throws StripeException;
}
