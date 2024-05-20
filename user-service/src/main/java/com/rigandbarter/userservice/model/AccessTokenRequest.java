package com.rigandbarter.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenRequest {
    private String grant_type;
    private String client_id;
    private String client_secret;
}
