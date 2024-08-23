package com.rigandbarter.paymentservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

// TODO: May split this out in the future to multiple classes

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
    private String accountId;
    private boolean verified;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "userId")
    private List<StripePaymentMethod> paymentMethods;
}
