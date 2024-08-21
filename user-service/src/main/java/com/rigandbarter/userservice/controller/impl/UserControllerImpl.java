package com.rigandbarter.userservice.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        if(!principal.getClaimAsString("sub").equals(userId))
            throw new UserAuthorizationException("Current user does not have permission to access another user's info");

        log.info("Attempting to retrieve user: " + userId);
        return this.userService.getUserById(userId);
    }

    @Override
    public UserBasicInfoResponse getUserBasicInfo(String userId, Jwt principal) {
        return this.userService.getUserBasicInfoById(userId);
    }

    @Override
    public UserBasicInfoResponse setUserBasicInfo(String userId, String userInfoJson, MultipartFile profilePic, Jwt principal)
            throws UpdateUserException, JsonProcessingException {
        if(!principal.getClaimAsString("sub").equals(userId))
            throw new UserAuthorizationException("Current user does not have permission to modify another user's info");

        UserBasicInfoRequest userBasicInfoRequest = new ObjectMapper().readValue(userInfoJson, UserBasicInfoRequest.class);
        log.info("Updating info for user " + userId);
        return this.userService.setUserBasicInfo(userId, userBasicInfoRequest, profilePic.getSize() > 0 ? profilePic : null);
    }

    @Override
    public UserBillingInfoResponse setUserBillingInfo(String userId, UserBillingInfoRequest userBillingInfoRequest, Jwt principal)
            throws UpdateUserException {
        if(!principal.getClaimAsString("sub").equals(userId))
            throw new UserAuthorizationException("Current user does not have permission to modify another user's info");

        // TODO: Validate billing info is in valid format before attempting to save (i.e. not missing fields, not enough card digits, etc)
        log.info("Updating billing info for user: " + userId);
        return this.userService.setUserBillingInfo(userId, userBillingInfoRequest);
    }

    @Override
    public String healthCheck() {
        return "User service is running...";
    }
}
