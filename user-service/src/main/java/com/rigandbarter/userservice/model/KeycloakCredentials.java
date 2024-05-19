package com.rigandbarter.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakCredentials {
    private String type;
    private String value;
    private boolean temporary;
}
