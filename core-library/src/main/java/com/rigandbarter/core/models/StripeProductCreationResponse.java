package com.rigandbarter.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StripeProductCreationResponse {
    private String stripeProductId;
    private String stripePriceId;
}
