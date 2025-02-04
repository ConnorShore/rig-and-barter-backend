package com.rigandbarter.paymentservice.repository;

import com.rigandbarter.paymentservice.model.StripeProduct;

public interface IStripeProductRepository {

    /**
     * Saves the stripe product to the database
     * @param stripeProduct The stripe product to save
     * @return The transaction if saved, null otherwise
     */
    StripeProduct save(StripeProduct stripeProduct);

    /**
     * Finds the stripe product with specified id
     * @param stripeProductId The stripe product id of the stripe product to find
     * @return The transacitno if found
     */
    StripeProduct findByStripeProductId(String stripeProductId);
}
