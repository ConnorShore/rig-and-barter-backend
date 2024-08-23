package com.rigandbarter.paymentservice.controller.impl;

import com.rigandbarter.paymentservice.controller.IPaymentController;
import com.rigandbarter.paymentservice.dto.StripePaymentMethodRequest;
import com.rigandbarter.core.models.StripePaymentMethodResponse;
import com.rigandbarter.core.models.StripeCustomerResponse;
import com.rigandbarter.paymentservice.dto.StripeProductRequest;
import com.rigandbarter.paymentservice.service.IPaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentControllerImpl implements IPaymentController {

    private final IPaymentService paymentService;

    @Override
    public StripePaymentMethodResponse addPaymentMethod(StripePaymentMethodRequest paymentMethodRequest, Jwt principal) throws StripeException {
        return paymentService.addPaymentMethod(principal.getSubject(), paymentMethodRequest);
    }

    @Override
    public String createProduct(StripeProductRequest productRequest) throws StripeException {
        return paymentService.createStripeProduct(productRequest);
    }

    @Override
    public String createStripeAccountForUser(Jwt principal) throws StripeException {
        return paymentService.createStripeCustomerAccount(principal.getSubject());
    }

    @Override
    public StripeCustomerResponse getStripeCustomerInfo(Jwt principal) throws AuthenticationException {
        return paymentService.getStripeCustomerInfo(principal.getSubject());
    }

    @Override
    public void deleteStripeAccount(String accountId) throws StripeException {
        paymentService.deleteStripeAccount(accountId);
    }

    @Override
    public void handleStripeAccountReAuth() {
        System.out.println("REAUTH CALLED");
    }

    @Override
    public String healthCheck() {
        return "Payment service is running...";
    }
}
