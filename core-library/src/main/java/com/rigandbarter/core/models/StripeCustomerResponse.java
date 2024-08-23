package com.rigandbarter.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StripeCustomerResponse {
    private String userId;
    private String stripeId;
    private String accountId;
    private boolean verified;
    private List<StripePaymentMethodResponse> paymentMethods;
}
