package com.rigandbarter.userservice.mapper;

import com.rigandbarter.userservice.dto.UserRegisterRequest;
import com.rigandbarter.userservice.model.UserEntity;

public class UserMapper {

    /**
     * Maps the user register request to a user entity object
     * Note the profile picture fields are null as the user doesnt' set profile pic upon registration
     * @param userRegisterRequest The user info to convert to a user entity object
     * @return A user entity object to be used in the db
     */
    public static UserEntity dtoToEntity(UserRegisterRequest userRegisterRequest) {
        return UserEntity.builder()
//                .userId(userRegisterRequest.getUserId())
                .email(userRegisterRequest.getEmail())
                .firstName(userRegisterRequest.getFirstName())
                .lastName(userRegisterRequest.getLastName())
                .profilePictureId(null)
                .profilePictureUrl(null)
                .build();
    }
}
