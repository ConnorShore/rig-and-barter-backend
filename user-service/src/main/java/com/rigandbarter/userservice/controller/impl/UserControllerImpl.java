package com.rigandbarter.userservice.controller.impl;

import com.rigandbarter.userservice.controller.IUserController;
import com.rigandbarter.userservice.dto.UserRegisterRequest;
import com.rigandbarter.userservice.dto.UserResponse;
import com.rigandbarter.userservice.service.IKeycloakService;
import com.rigandbarter.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserControllerImpl implements IUserController {

    private final IUserService userService;

    @Override
    public String registerUser(UserRegisterRequest userRegisterRequest) {
        System.out.println();
        return this.userService.registerUser(userRegisterRequest);
    }

    @Override
    public String getUserTest(Jwt principal) {
        System.out.println();
        return "get user test ret";
    }

    @Override
    public UserResponse getUser(String userId) {
        return null;
    }

    @Override
    public String healthCheck() {
        return "User service is running...";
    }
}
