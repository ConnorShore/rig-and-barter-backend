package com.rigandbarter.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBillingInfo {
    private String userId;
    private String stripeCardToken;
//    private String cardNumber;
//    private String expirationDate;
//    private String cvv;
}
