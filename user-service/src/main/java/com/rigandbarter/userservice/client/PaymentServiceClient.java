package com.rigandbarter.userservice.client;

import com.rigandbarter.core.models.StripeCustomerResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

public interface PaymentServiceClient {

    @GetExchange(value = "/api/payment/profile")
    StripeCustomerResponse getPaymentProfile(@RequestHeader("Authorization") String bearerToken);
}
