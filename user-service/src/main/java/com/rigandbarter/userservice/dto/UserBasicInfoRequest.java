package com.rigandbarter.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicInfoRequest {
    private String firstName;
    private String lastName;
    private String email;
//    private String password;  // TODO: Be able to reset password in user basic info
}
