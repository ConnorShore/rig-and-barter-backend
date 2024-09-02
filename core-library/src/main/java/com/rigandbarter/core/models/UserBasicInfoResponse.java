package com.rigandbarter.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicInfoResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String profilePictureUrl;
//    private String password;  // TODO: Be able to reset password in user basic info
}
