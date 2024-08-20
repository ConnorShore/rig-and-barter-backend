package com.rigandbarter.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StripeProductRequest {
    private String name;
    private String description;
    private Double productPrice;
}
