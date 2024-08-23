package com.rigandbarter.userservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_user")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePictureId;
    private String profilePictureUrl;
    private String stripeCustomerId;
}
