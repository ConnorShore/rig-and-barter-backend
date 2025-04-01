package com.rigandbarter.paymentservice.repository;

import com.rigandbarter.paymentservice.model.StripePaymentMethod;
import com.rigandbarter.paymentservice.model.StripeProduct;

public interface IStripePaymentMethodRepository {

    /**
     * Deletes the stripe payment method
     * @param stripePaymentMethod The stripe payment method to delete
     */
    void delete(StripePaymentMethod stripePaymentMethod);
}
