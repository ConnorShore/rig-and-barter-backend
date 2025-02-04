package com.rigandbarter.listingservice.client;

import com.rigandbarter.listingservice.dto.StripeProductRequest;
import com.rigandbarter.core.models.StripeProductCreationResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

public interface PaymentServiceClient {

    @PostExchange(value = "/api/payment/product")
    StripeProductCreationResponse getPaymentProduct(@RequestBody StripeProductRequest request, @RequestHeader("Authorization") String bearerToken);

    @PutExchange(value = "/api/payment/product/{productId}/price")
    void updateProductPrice(@PathVariable String productId, @RequestBody double price, @RequestHeader("Authorization") String bearerToken);
}
