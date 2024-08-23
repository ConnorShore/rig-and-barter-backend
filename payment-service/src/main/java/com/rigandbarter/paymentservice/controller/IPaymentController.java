package com.rigandbarter.paymentservice.controller;


import com.rigandbarter.paymentservice.dto.StripePaymentMethodRequest;
import com.rigandbarter.core.models.StripePaymentMethodResponse;
import com.rigandbarter.core.models.StripeCustomerResponse;
import com.rigandbarter.paymentservice.dto.StripeProductRequest;
import com.stripe.exception.StripeException;
import org.apache.http.auth.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/payment")
public interface IPaymentController {

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    StripePaymentMethodResponse addPaymentMethod(@RequestBody StripePaymentMethodRequest paymentMethodRequest, @AuthenticationPrincipal Jwt principal) throws StripeException;

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
     * Creates a stripe connected account for the user
     * @param principal The user to create the account for
     * @return The redirect url to set the account info via stripe
     */
    @PutMapping("account")
    @ResponseStatus(HttpStatus.OK)
    String createStripeAccountForUser(@AuthenticationPrincipal Jwt principal) throws StripeException;

    /**
     * Gets the user's stripe information
     * @param principal The user auth info
     * @return the user's stripe info
     */
    @GetMapping("profile")
    @ResponseStatus(HttpStatus.OK)
    StripeCustomerResponse getStripeCustomerInfo(@AuthenticationPrincipal Jwt principal) throws AuthenticationException;

    /**
     * Deletes an account with specified id
     * @param accountId the id of the account to delete
     * @throws StripeException Fails to delete the account
     */
    @GetMapping("account/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    void deleteStripeAccount(@PathVariable String accountId) throws StripeException;

    /**
     * Reauth endpoint for account creation
     */
    @GetMapping("reauth")
    @ResponseStatus(HttpStatus.OK)
    void handleStripeAccountReAuth();

    /**
     * Status endpoint to see if service is running
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
