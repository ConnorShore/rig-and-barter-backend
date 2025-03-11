package com.rigandbarter.userservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rigandbarter.core.models.UserBasicInfoResponse;
import com.rigandbarter.core.models.UserResponse;
import com.rigandbarter.userservice.dto.*;
import com.rigandbarter.userservice.util.exceptions.UpdateUserException;
import com.rigandbarter.userservice.util.exceptions.UserRegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
     * Checks if the user is verified
     * @param userId The user to see if verified
     * @return True if the specified user is verified, false otherwise
     */
    @GetMapping("{userId}/verified")
    @ResponseStatus(HttpStatus.OK)
    boolean isUserVerified(@PathVariable  String userId, @AuthenticationPrincipal Jwt principal);

    /**
     * Health check for the service
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
