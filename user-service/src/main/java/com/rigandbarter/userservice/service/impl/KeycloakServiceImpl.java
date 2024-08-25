package com.rigandbarter.userservice.service.impl;

import com.rigandbarter.userservice.dto.KeycloakUser;
import com.rigandbarter.userservice.dto.KeycloakUserCredentialsResponse;
import com.rigandbarter.userservice.dto.UserRegisterRequest;
import com.rigandbarter.userservice.model.AccessTokenResponse;
import com.rigandbarter.userservice.model.KeycloakCredentials;
import com.rigandbarter.userservice.model.KeycloakUserRepresentation;
import com.rigandbarter.userservice.service.IKeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakServiceImpl implements IKeycloakService {

    private final WebClient.Builder webClientBuilderNoLb;

    @Value("${rb.keycloak.url}")
    private String KEYCLOAK_URL;

    @Value("${rb.keycloak.admin_client_id}")
    private String ADMIN_CLIENT_ID;

    @Value("${rb.keycloak.admin_client_secret}")
    private String ADMIN_CLIENT_SECRET;

    public String registerUser(UserRegisterRequest userRegisterRequest) {
        final String userEndpoint = "/admin/realms/rig-and-barter-realm/users";
        String url = KEYCLOAK_URL + userEndpoint;

        var accessToken = getAccessToken();

        KeycloakCredentials userCredentials = (KeycloakCredentials.builder()
                .type("password")
                .temporary(false)
                .value(userRegisterRequest.getPassword())
                .build());

        KeycloakUserRepresentation userRep = KeycloakUserRepresentation.builder()
                .enabled(true)
                .username(userRegisterRequest.getEmail())
                .email(userRegisterRequest.getEmail())
                .firstName(userRegisterRequest.getFirstName())
                .lastName(userRegisterRequest.getLastName())
                .credentials(List.of(userCredentials))
                .build();

        String userId;
        try {
            webClientBuilderNoLb.build()
                    .post()
                    .uri(url)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                    .bodyValue(userRep)
                    .retrieve()
                    .bodyToMono(KeycloakUserCredentialsResponse.class)
                    .block();

            KeycloakUser createdUser = getUserByEmail(userRep.getEmail());
            userId = createdUser.getId();
        } catch (Exception e) {
            log.error("Failed to register user with keycloak: " + e.getMessage());
            return null;
        }

        return userId;
    }

    @Override
    public KeycloakUser getUserByEmail(String email) {
        final String userEndpoint = "/admin/realms/rig-and-barter-realm/users?email=" + email;
        String url = KEYCLOAK_URL + userEndpoint;

        var accessToken = getAccessToken();

        List<KeycloakUser> keycloakUserList = webClientBuilderNoLb.build()
                .get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<KeycloakUser>>() { })
                .block();

        assert keycloakUserList != null && !keycloakUserList.isEmpty();
        return keycloakUserList.get(0);
    }


    /**
     * TODO: Maybe move this to a bean?
     */
    private String getAccessToken() {
        final String TOKEN_ENDPOINT = "/realms/rig-and-barter-realm/protocol/openid-connect/token";

        String url = KEYCLOAK_URL + TOKEN_ENDPOINT;

        var credentialData = webClientBuilderNoLb.build()
                .post()
                .uri(url)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("client_id", ADMIN_CLIENT_ID)
                        .with("client_secret", ADMIN_CLIENT_SECRET))
                .retrieve()
                .bodyToMono(AccessTokenResponse.class)
                .block();

        assert credentialData != null;
        return credentialData.getAccess_token();
    }
}
