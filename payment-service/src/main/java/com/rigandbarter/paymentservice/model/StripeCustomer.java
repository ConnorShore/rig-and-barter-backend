package com.rigandbarter.paymentservice.model;

import jakarta.persistence.*;
import lombok.*;


// TODO: May split this out in the future to multiple clsses

@Entity
@Table(name = "t_stripe_customer")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StripeCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId;
    private String stripeId;
    private String name;
    private String email;

    private String paymentId;
    private String cardLast4;
    private Long expirationMonth;
    private Long expirationYear;
    private String cardCvv;
}
