package com.rigandbarter.userservice.model;

import jakarta.persistence.*;
import lombok.*;

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
    private String nameOnCard;
    private String cardNumber;
    private String expirationDate;
    private String cvv;
}
