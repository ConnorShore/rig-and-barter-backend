package com.rigandbarter.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
//    private String userId;  // The keycloak user id. Need to set callback to this service for registrant user in our db
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
