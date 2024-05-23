package com.rigandbarter.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rigandbarter.userservice.dto.UserBasicInfoRequest;
import com.rigandbarter.userservice.dto.UserRegisterRequest;
import com.rigandbarter.userservice.dto.UserResponse;
import com.rigandbarter.userservice.util.exceptions.UpdateUserException;
import com.rigandbarter.userservice.util.exceptions.UserRegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequestMapping("api/user")
public interface IUserController {

    /**
     * Registers a new user in the user service and database
     * @param userRegisterRequest The info with the user to create (includes keycloak id)
     * @return The userID of the created user
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse registerUser(@RequestBody UserRegisterRequest userRegisterRequest) throws UserRegistrationException;

    /**
     * Gets all the user's info
     * @param userId The id of the user to get info for
     * @return The user's info
     */
    @GetMapping("{userId}")
    @ResponseStatus(HttpStatus.OK)
    UserResponse getUser(@PathVariable String userId);

    /**
     * Sets the user's basic profile information
     * @return
     */
    @PostMapping("{userId}/info/basic")
    @ResponseStatus(HttpStatus.OK)
    UserResponse setUserBasicInfo(@PathVariable String userId,
                                  @RequestPart(name = "userInfo") String userInfoJson,
                                  @RequestPart(name = "profilePic") MultipartFile profilePic) throws UpdateUserException, JsonProcessingException;

    /**
     * Health check for the service
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
