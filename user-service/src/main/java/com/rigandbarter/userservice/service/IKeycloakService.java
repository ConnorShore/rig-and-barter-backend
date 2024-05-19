package com.rigandbarter.userservice.service;

import com.rigandbarter.userservice.dto.UserRegisterRequest;

public interface IKeycloakService {
    void registerUser(UserRegisterRequest userRegisterRequest);
}
