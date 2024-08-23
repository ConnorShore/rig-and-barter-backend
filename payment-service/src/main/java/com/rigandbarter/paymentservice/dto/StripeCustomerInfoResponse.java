package com.rigandbarter.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StripeCustomerInfoResponse {
    private String userId;
    private String stripeId;
    private String paymentId;
    private String accountId;
}
