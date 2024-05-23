package com.rigandbarter.userservice.mapper;

import com.rigandbarter.userservice.dto.UserBasicInfoRequest;
import com.rigandbarter.userservice.dto.UserRegisterRequest;
import com.rigandbarter.userservice.model.UserEntity;

public class UserMapper {

    /**
     * Maps the user basic info request to a user entity object
     * @param userBasicInfoRequest The user info to convert to a user entity object
     * @return A user entity object to be used in the db
     */
    public static UserEntity basicInfoDtoToEntity(UserBasicInfoRequest userBasicInfoRequest, String userId) {
        return UserEntity.builder()
                .userId(userId)
                .email(userBasicInfoRequest.getEmail())
                .firstName(userBasicInfoRequest.getFirstName())
                .lastName(userBasicInfoRequest.getLastName())
                .build();
    }
}
