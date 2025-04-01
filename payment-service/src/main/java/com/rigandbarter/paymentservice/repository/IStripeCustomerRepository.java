package com.rigandbarter.paymentservice.repository;

import com.rigandbarter.paymentservice.model.StripeCustomer;
import com.rigandbarter.paymentservice.model.StripeProduct;

public interface IStripeCustomerRepository {

    /**
     * Saves the stripe customer to the database
     * @param stripeCustomer The stripe customer to save
     * @return The customer if saved, null otherwise
     */
    StripeCustomer save(StripeCustomer stripeCustomer);

    /**
     * Finds the stripe customer with specified user id
     * @param userId The user id of the stripe customer to find
     * @return The customer if found
     */
    StripeCustomer findByUserId(String userId);

    /**
     * Deletes the stripe customer
     * @param stripeCustomer The customer to delete
     */
    void delete(StripeCustomer stripeCustomer);
}
