package com.rigandbarter.paymentservice.service;

import com.rigandbarter.paymentservice.dto.StripeProductRequest;
import com.stripe.exception.StripeException;

public interface IPaymentService {

    /**
     * Creates a product in Stripe
     * @param stripeProductRequest The details of the product
     * @return The Stripe id of the product
     * @throws StripeException Fails to create product/price in Stripe
     */
    String createStripeProduct(StripeProductRequest stripeProductRequest) throws StripeException;
}
