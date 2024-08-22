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
    private String nameOnCard;
    private String cardNumber;
    private String expirationDate;
    private String cvv;
}
