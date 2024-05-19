package com.rigandbarter.userservice.controller;

import com.rigandbarter.userservice.dto.UserRegisterRequest;
import com.rigandbarter.userservice.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("api/user")
public interface IUserController {

    // TODO: THis may need to be a callback method instead for keycloak to callback to once user is registered

    /**
     * Registers a new user in the user service and database
     * @param userRegisterRequest The info with the user to create (includes keycloak id)
     * @return The userID of the created user
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String registerUser(@RequestBody UserRegisterRequest userRegisterRequest);

    /**
     * Gets all the user's info
     * @return The user's info
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public String getUserTest(@AuthenticationPrincipal Jwt principal);

    /**
     * Gets all the user's info
     * @param userId The id of the user to get info for
     * @return The user's info
     */
    @GetMapping("{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser(@PathVariable String userId);

    /**
     * Health check for the service
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    public String healthCheck();
}
