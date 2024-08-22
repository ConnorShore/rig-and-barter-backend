package com.rigandbarter.userservice.model;

import jakarta.persistence.*;
import lombok.*;

//TODO: Might want to make this a paymentMethod class so user can have multiple cards and this just tracks the card
@Entity
@Table(name = "t_billing_info")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
//    private String cardLast4;
    private String stripeCardToken;
}
