package com.rigandbarter.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StripePaymentMethodResponse {
    private String stripePaymentId;
    private String cardToken;
    private String nameOnCard;
    private String last4Digits;
    private Long expirationMonth;
    private Long expirationYear;
}
