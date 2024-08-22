package com.rigandbarter.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StripeProductRequest {
    private String userId;
    private String name;
    private String description;
    private Double productPrice;
}
