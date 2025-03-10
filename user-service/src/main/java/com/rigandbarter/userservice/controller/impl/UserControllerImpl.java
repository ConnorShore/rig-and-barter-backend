package com.rigandbarter.userservice.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.core.models.UserBasicInfoResponse;
import com.rigandbarter.core.models.UserResponse;
import com.rigandbarter.core.util.exceptions.UserAuthorizationException;
import com.rigandbarter.userservice.controller.IUserController;
import com.rigandbarter.userservice.dto.*;
import com.rigandbarter.userservice.service.IUserService;
import com.rigandbarter.userservice.util.exceptions.UpdateUserException;
import com.rigandbarter.userservice.util.exceptions.UserRegistrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserControllerImpl implements IUserController {

    private final IUserService userService;

    @Override
    public UserResponse registerUser(UserRegisterRequest userRegisterRequest) throws UserRegistrationException {
        log.info("Registering new user with email: " + userRegisterRequest.getEmail());
        return this.userService.registerUser(userRegisterRequest);
    }

    @Override
    public UserResponse getUser(String userId, Jwt principal) {
        if(!userId.equals(principal.getSubject()))
            throw new UserAuthorizationException("Current user does not have permission to access another user's info");

        log.info("Attempting to retrieve user: " + userId);
        return this.userService.getUserById(userId, principal);
    }

    @Override
    public UserBasicInfoResponse getUserBasicInfo(String userId, Jwt principal) {
        if(!userId.equals(principal.getSubject()))
            throw new UserAuthorizationException("Current user does not have permission to access another user's info");

        return this.userService.getUserBasicInfoById(userId);
    }

    @Override
    public boolean isUserVerified(String userId, Jwt principal) {
        return this.userService.isUserVerified(userId, principal);
    }

    @Override
    public UserBasicInfoResponse setUserBasicInfo(String userId, String userInfoJson, MultipartFile profilePic, Jwt principal)
            throws UpdateUserException, JsonProcessingException {
        if(!userId.equals(principal.getSubject()))
            throw new UserAuthorizationException("Current user does not have permission to access another user's info");

        UserBasicInfoRequest userBasicInfoRequest = new ObjectMapper().readValue(userInfoJson, UserBasicInfoRequest.class);
        log.info("Updating info for user " + userId);
        return this.userService.setUserBasicInfo(userId, userBasicInfoRequest, profilePic.getSize() > 0 ? profilePic : null);
    }

    @Override
    public String healthCheck() {
        return "User service is running...";
    }
}
