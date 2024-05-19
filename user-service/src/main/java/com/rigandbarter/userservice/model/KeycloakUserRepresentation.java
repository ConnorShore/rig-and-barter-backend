package com.rigandbarter.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakUserRepresentation {
    private boolean enabled;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<KeycloakCredentials> credentials;
}
