package com.rigandbarter.paymentservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_stripe_product")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StripeProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId; // Listing userId (may want to use their stripe id?)
    private String stripeProductId;
    private String stripePriceId;
    private String name;
    private String description;
    private String currency;
    private Long priceInCents;
}
