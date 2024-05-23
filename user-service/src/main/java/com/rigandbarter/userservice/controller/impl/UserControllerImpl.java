package com.rigandbarter.userservice.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.userservice.controller.IUserController;
import com.rigandbarter.userservice.dto.UserBasicInfoRequest;
import com.rigandbarter.userservice.dto.UserRegisterRequest;
import com.rigandbarter.userservice.dto.UserResponse;
import com.rigandbarter.userservice.service.IUserService;
import com.rigandbarter.userservice.util.exceptions.UpdateUserException;
import com.rigandbarter.userservice.util.exceptions.UserRegistrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public UserResponse getUser(String userId) {
        log.info("Attempting to retrieve user: " + userId);
        return this.userService.getUserByEmail(userId);
    }

    @Override
    public UserResponse setUserBasicInfo(String userId, String userInfoJson, MultipartFile profilePic) throws UpdateUserException, JsonProcessingException {
        UserBasicInfoRequest userBasicInfoRequest = new ObjectMapper().readValue(userInfoJson, UserBasicInfoRequest.class);
        log.info("Updating info for user " + userId);
        return this.userService.setUserBasicInfo(userId, userBasicInfoRequest, profilePic.getSize() > 0 ? profilePic : null);
    }

    @Override
    public String healthCheck() {
        return "User service is running...";
    }
}
