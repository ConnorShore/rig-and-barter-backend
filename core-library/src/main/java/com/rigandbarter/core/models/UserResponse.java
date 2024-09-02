package com.rigandbarter.core.models;

import com.rigandbarter.core.models.StripeCustomerResponse;
import com.rigandbarter.core.models.UserBasicInfoResponse;
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
    private StripeCustomerResponse stripeInfo;
}
