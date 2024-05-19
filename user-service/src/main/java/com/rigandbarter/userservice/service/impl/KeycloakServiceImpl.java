package com.rigandbarter.userservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.userservice.dto.KeycloakUserCredentialsResponse;
import com.rigandbarter.userservice.dto.UserRegisterRequest;
import com.rigandbarter.userservice.model.AccessTokenRequest;
import com.rigandbarter.userservice.model.AccessTokenResponse;
import com.rigandbarter.userservice.model.KeycloakCredentials;
import com.rigandbarter.userservice.model.KeycloakUserRepresentation;
import com.rigandbarter.userservice.service.IKeycloakService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class KeycloakServiceImpl implements IKeycloakService {

    private final WebClient.Builder webClientBuilder;   // TODO: Move to core-library

    private String getAccessToken() {

        String url = "http://localhost:8180/realms/rig-and-barter-realm/protocol/openid-connect/token";
//        AccessTokenRequest request = AccessTokenRequest.builder()
//                .grant_type("client_credentials")
//                .client_id("rig-and-barter-client")
//                .client_secret("dE7irq4Kj44QbiftdLvi1q9tHLpWMlRL")
//                .build();

//        var ret = webClientBuilder.build()
//                .post()
//                .uri(url)
//                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
//                        .with("client_id", "rig-and-barter-client")
//                        .with("client_secret", "dE7irq4Kj44QbiftdLvi1q9tHLpWMlRL"))
//                .body(BodyInserters.fromFormData("grant_type", "password")
//                        .with("client_id", "rig-and-barter-client")
//                        .with("username", "admin")
//                        .with("password", "admin")
//                        .with("scope", "openid"))
//                .retrieve()
//                .bodyToMono(AccessTokenResponse.class)
//                .block();

        var ret = webClientBuilder.build()
                .post()
                .uri(url)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("client_id", "admin-cli")
                        .with("client_secret", "t8TYbmn5YFt68dBMgcJzQEIKJQi7sJYz"))
//                        .with("scope", "openid"))
                .retrieve()
                .bodyToMono(AccessTokenResponse.class)
                .block();

        return ret.getAccess_token();
    }

    public void registerUser(UserRegisterRequest userRegisterRequest) {
        var accessToken = getAccessToken();

        String url = "http://localhost:8180/admin/realms/rig-and-barter-realm/users";
        KeycloakCredentials creds = (KeycloakCredentials.builder()
                .type("password")
                .temporary(false)
                .value(userRegisterRequest.getPassword())
                .build());
        List<KeycloakCredentials> allCreds = new ArrayList<>();
        allCreds.add(creds);
        KeycloakUserRepresentation userRep = KeycloakUserRepresentation.builder()
                .enabled(true)
                .username(userRegisterRequest.getEmail())
                .email(userRegisterRequest.getEmail())
                .firstName(userRegisterRequest.getFirstName())
                .lastName(userRegisterRequest.getLastName())
                .credentials(allCreds)
                .build();

        String strBody = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            strBody = mapper.writeValueAsString(userRep);
            System.out.println();
        } catch (Exception e) {
            System.out.println();
        }

        var val = webClientBuilder.build()
                .post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .bodyValue(userRep);
//                .bodyValue(BodyInserters.fromValue(userRep));

        var ret = val.retrieve()
                .bodyToMono(KeycloakUserCredentialsResponse.class)
                .block();

        System.out.println();
    }
}
