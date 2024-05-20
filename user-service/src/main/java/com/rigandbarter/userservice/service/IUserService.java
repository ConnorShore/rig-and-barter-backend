package com.rigandbarter.userservice.service;

import com.rigandbarter.userservice.dto.UserRegisterRequest;
import com.rigandbarter.userservice.dto.UserResponse;
import com.rigandbarter.userservice.util.exceptions.UserRegistrationException;

public interface IUserService {

    /**
     * Registers a new user in the system
     * @param userRegisterRequest The info of the user to register
     * @return The userId of the registered user
     */
    UserResponse registerUser(UserRegisterRequest userRegisterRequest) throws UserRegistrationException;

    /**
     * Returns the user with the specified email
     * @param email The email of the user to return
     * @return Thhe user with the specified email, null if none exist
     */
    UserResponse getUserByEmail(String email);
}
