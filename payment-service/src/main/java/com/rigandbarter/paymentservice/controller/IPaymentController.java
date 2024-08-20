package com.rigandbarter.paymentservice.controller;


import com.rigandbarter.paymentservice.dto.StripeProductRequest;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/payment")
public interface IPaymentController {

    /**
     * Creates a product in Stripe
     * @param productRequest The details of the product to create
     * @return The Stripe id of the created product
     * @throws StripeException Fails to create product/price in Stripe
     */
    @PostMapping("product")
    @ResponseStatus(HttpStatus.CREATED)
    String createProduct(@RequestBody StripeProductRequest productRequest) throws StripeException;

    /**
     * Status endpoint to see if service is running
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
