package com.rigandbarter.userservice.service.impl;

import com.rigandbarter.userservice.dto.KeycloakUser;
import com.rigandbarter.userservice.dto.UserResponse;
import com.rigandbarter.userservice.model.UserEntity;
import com.rigandbarter.userservice.repository.relational.IUserRepository;
import com.rigandbarter.userservice.dto.UserRegisterRequest;
import com.rigandbarter.userservice.repository.file.IProfilePictureRepository;
import com.rigandbarter.userservice.service.IKeycloakService;
import com.rigandbarter.userservice.service.IUserService;
import com.rigandbarter.userservice.util.exceptions.UserRegistrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {

    private final IKeycloakService keycloakService;

    private final IUserRepository userRepository;
    private final IProfilePictureRepository profilePictureRepository;

    @Override
    public UserResponse registerUser(UserRegisterRequest userRegisterRequest) throws UserRegistrationException {
        // Register user with keycloak
        String userId = this.keycloakService.registerUser(userRegisterRequest);
        if(userId == null)
            throw new UserRegistrationException("Failed to register user with keycloak: " + userRegisterRequest.getEmail());

        // Add user to database
        try {
            UserEntity userEntity = UserEntity.builder()
                    .userId(userId)
                    .email(userRegisterRequest.getEmail())
                    .firstName(userRegisterRequest.getFirstName())
                    .lastName(userRegisterRequest.getLastName())
                    .profilePictureId(null)
                    .profilePictureUrl(null)
                    .build();

            userRepository.save(userEntity);
        } catch (Exception e) {
            throw new UserRegistrationException("Failed to create user in database: " + userRegisterRequest.getEmail());
        }

        return UserResponse.builder()
                .id(userId)
                .email(userRegisterRequest.getEmail())
                .firstName(userRegisterRequest.getFirstName())
                .lastName(userRegisterRequest.getLastName())
                .profilePictureUrl(null)
                .build();
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        UserEntity userEntity = this.userRepository.findByEmail(email);

        // TODO: Get user profile picture from file db

        return UserResponse.builder()
                .id(userEntity.getUserId())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .profilePictureUrl(null)
                .build();
    }
}
