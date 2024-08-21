package com.rigandbarter.userservice.service;

import com.rigandbarter.userservice.dto.*;
import com.rigandbarter.userservice.util.exceptions.UpdateUserException;
import com.rigandbarter.userservice.util.exceptions.UserRegistrationException;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {

    /**
     * Registers a new user in the system
     * @param userRegisterRequest The info of the user to register
     * @return The userId of the registered user
     */
    UserResponse registerUser(UserRegisterRequest userRegisterRequest) throws UserRegistrationException;

    /**
     * Returns the user with the specified uid
     * @param userId The uid of the user to return
     * @return The user with the specified uid, null if none exist
     */
    UserResponse getUserById(String userId);

    /**
     * Returns the user's (with the specified uid) basic info
     * @param userId The uid of the user to return
     * @return The user with the specified uid, null if none exist
     */
    UserBasicInfoResponse getUserBasicInfoById(String userId);

    /**
     * Updates the user info and returns the updated user
     * @param userId The id of the user to update
     * @param userBasicInfoRequest The basic info of the user
     * @param profilePic The profile picture of the user (null if not updated)
     * @return The updated user
     */
    UserBasicInfoResponse setUserBasicInfo(String userId,
                                           UserBasicInfoRequest userBasicInfoRequest,
                                           MultipartFile profilePic) throws UpdateUserException;

    /**
     * Updates the user info and returns the
     * @param userId
     * @param userBillingInfoRequest
     * @return
     */
    UserBillingInfoResponse setUserBillingInfo(String userId,
                                               UserBillingInfoRequest userBillingInfoRequest) throws UpdateUserException;
}
