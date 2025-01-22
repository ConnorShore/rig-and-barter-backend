package com.rigandbarter.listingservice.client;

import com.rigandbarter.core.models.UserResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

public interface UserServiceClient {

    @GetExchange(value = "/api/user/{userId}")
    UserResponse getUser(@PathVariable String userId, @RequestHeader("Authorization") String bearerToken);
}
