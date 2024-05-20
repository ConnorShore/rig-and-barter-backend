package com.rigandbarter.userservice.service;

import com.rigandbarter.userservice.dto.KeycloakUser;
import com.rigandbarter.userservice.dto.UserRegisterRequest;

public interface IKeycloakService {

    /**
     * Registers a user with keycloak
     * @param userRegisterRequest The info of the user to register with keycloak
     * @return The uid of the created user
     */
    String registerUser(UserRegisterRequest userRegisterRequest);

    /**
     * Gets the keycloak user with the specified email
     * @param email The email of the user to retrieve from keycloak
     * @return The keycloak user with the specified email, null otherwise
     */
    KeycloakUser getUserByEmail(String email);
}
