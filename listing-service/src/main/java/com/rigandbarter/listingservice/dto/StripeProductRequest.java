package com.rigandbarter.listingservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StripeProductRequest {
    private String userId;
    private String name;
    private String description;
    private Double productPrice;
}
