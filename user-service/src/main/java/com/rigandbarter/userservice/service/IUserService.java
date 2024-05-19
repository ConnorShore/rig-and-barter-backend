package com.rigandbarter.userservice.service;

import com.rigandbarter.userservice.dto.UserRegisterRequest;

public interface IUserService {

    /**
     * Registers a new user in the system
     * @param userRegisterRequest The info of the user to register
     * @return The userId of the registered user
     */
    String registerUser(UserRegisterRequest userRegisterRequest);
}
