package com.rigandbarter.listingservice.client;

import com.rigandbarter.core.models.StripeCustomerResponse;
import com.rigandbarter.listingservice.dto.StripeProductRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;

public interface PaymentServiceClient {

    @PostExchange(value = "/api/payment/product")
    String getPaymentProduct(@RequestBody StripeProductRequest request, @RequestHeader("Authorization") String bearerToken);
}
