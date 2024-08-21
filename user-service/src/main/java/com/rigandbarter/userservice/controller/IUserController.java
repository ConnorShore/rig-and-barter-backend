package com.rigandbarter.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rigandbarter.userservice.dto.*;
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
    UserResponse getUser(@PathVariable String userId, @AuthenticationPrincipal Jwt principal);

    /**
     * Sets the user's basic profile information
     * @return The user's basic info
     */
    @PostMapping("{userId}/info/basic")
    @ResponseStatus(HttpStatus.OK)
    UserBasicInfoResponse setUserBasicInfo(@PathVariable String userId,
                                           @RequestPart(name = "userInfo") String userInfoJson,
                                           @RequestPart(name = "profilePic") MultipartFile profilePic,
                                           @AuthenticationPrincipal Jwt principal) throws UpdateUserException, JsonProcessingException;

    /**
     * Gets the user's basic info
     * @param userId the id of the user to get info for
     * @return The user's basic info
     */
    @GetMapping("{userId}/info/basic")
    @ResponseStatus(HttpStatus.OK)
    UserBasicInfoResponse getUserBasicInfo(@PathVariable String userId, @AuthenticationPrincipal Jwt principal);

    /**
     * Sets the user's billing information
     * @param userId The id of the user to set the info for
     * @param userBillingInfoRequest The billing information
     * @param principal The JWT info
     * @return The user's billing information
     * @throws UpdateUserException
     */
    @PostMapping("{userId}/info/billing")
    @ResponseStatus(HttpStatus.OK)
    UserBillingInfoResponse setUserBillingInfo(@PathVariable String userId,
                                           @RequestBody UserBillingInfoRequest userBillingInfoRequest,
                                           @AuthenticationPrincipal Jwt principal) throws UpdateUserException;

    /**
     * Health check for the service
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
