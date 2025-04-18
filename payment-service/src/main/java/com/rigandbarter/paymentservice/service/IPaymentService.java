package com.rigandbarter.paymentservice.service;

import com.rigandbarter.core.models.UserBasicInfo;
import com.rigandbarter.eventlibrary.events.TransactionCompletedEvent;
import com.rigandbarter.paymentservice.dto.StripePaymentMethodRequest;
import com.rigandbarter.core.models.StripePaymentMethodResponse;
import com.rigandbarter.core.models.StripeCustomerResponse;
import com.rigandbarter.core.models.StripeProductCreationResponse;
import com.rigandbarter.paymentservice.dto.StripeProductRequest;
import com.rigandbarter.paymentservice.model.StripeCustomer;
import com.stripe.exception.StripeException;

import javax.naming.AuthenticationException;

public interface IPaymentService {

    /**
     * Creates a product in Stripe and our db
     * @param stripeProductRequest The details of the product
     * @return The Stripe ids of the product and price
     * @throws StripeException Fails to create product/price in Stripe
     */
    StripeProductCreationResponse createStripeProduct(StripeProductRequest stripeProductRequest) throws StripeException;

    /**
     * Updates the price of a product in Stripe
     * @param productId The id of the product to update
     * @param price The new price of the product
     * @throws StripeException Fails to update the product price
     */
    void updateStripeProductPrice(String productId, double price) throws StripeException;

    /**
     * Deletes a product from Stripe
     * @param productId The id of the product to delete
     * @throws StripeException Fials to delete product/price in Stripe
     */
    void deleteStripeProduct(String productId) throws StripeException;

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
     * Gets the user's stripe information
     * @param userId The id of the user to get the stripe info for
     * @return The stripe information
     */
    StripeCustomerResponse getStripeCustomerInfo(String userId);

    /**
     * Deletes an account with the specified ID for the user
     * @param accountId The id of the account to delete
     * @param userId The user to delete the account for
     * @throws StripeException
     */
    void deleteStripeAccount(String accountId, String userId, boolean istest) throws AuthenticationException;

    /**
     * Adds payment method for stripe customer
     * @param userId The id of the user to add the payment method for
     * @param paymentMethodRequest The payment method info
     * @return Populated payment method object
     */
    StripePaymentMethodResponse addPaymentMethod(String userId, StripePaymentMethodRequest paymentMethodRequest) throws StripeException;

    /**
     * Deletes the payment from the user
     * @param paymentId The id of the payment to delete
     * @param userId The user to delete the payment from
     */
    void deletePaymentMethod(String paymentId, String userId) throws AuthenticationException;

    /**
     * Completes the setup intent in stripe for the buyer to seller
     * @param transactionCreatedEvent The transaction info
     */
    void completeTransaction(TransactionCompletedEvent transactionCreatedEvent) throws StripeException;

    /**
     * Deletes all user related resources from the payment service
     * @param userId The id of the user to delete resources for
     */
    void deleteUser(String userId);
}
