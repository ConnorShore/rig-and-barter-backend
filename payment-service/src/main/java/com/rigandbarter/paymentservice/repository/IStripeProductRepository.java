package com.rigandbarter.paymentservice.repository;

import com.rigandbarter.paymentservice.model.StripeProduct;

import java.util.List;

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

    /**
     * Finds all stripe products with specified user id
     * @param userId The id of the user to get products for
     * @return All products for the user
     */
    List<StripeProduct> findAllByUserId(String userId);

    /**
     * Deletes the stripe product
     * @param stripeProduct The stripe product to delete
     */
    void delete(StripeProduct stripeProduct);
}
