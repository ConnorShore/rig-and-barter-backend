package com.rigandbarter.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private UserBasicInfoResponse basicInfo;
    private UserBillingInfoResponse billingInfo;
}
