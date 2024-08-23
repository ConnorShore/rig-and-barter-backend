package com.rigandbarter.paymentservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_payment_method")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StripePaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId;
    private String stripePaymentId;
    private String cardToken;
    private String nameOnCard;
    private String last4Digits;
    private Long expirationMonth;
    private Long expirationYear;
}
