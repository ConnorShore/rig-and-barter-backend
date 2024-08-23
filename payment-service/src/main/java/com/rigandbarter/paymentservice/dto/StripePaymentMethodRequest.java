package com.rigandbarter.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StripePaymentMethodRequest {
    private String nickname;
    private String cardToken;
}
