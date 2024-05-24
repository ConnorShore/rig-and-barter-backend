package com.rigandbarter.userservice.service.impl;

import com.rigandbarter.userservice.dto.*;
import com.rigandbarter.userservice.model.BillingInfoEntity;
import com.rigandbarter.userservice.model.UserEntity;
import com.rigandbarter.userservice.repository.file.IProfilePictureRepository;
import com.rigandbarter.userservice.repository.relational.IBillingInfoRepository;
import com.rigandbarter.userservice.repository.relational.IUserRepository;
import com.rigandbarter.userservice.service.IKeycloakService;
import com.rigandbarter.userservice.service.IUserService;
import com.rigandbarter.userservice.util.exceptions.UpdateUserException;
import com.rigandbarter.userservice.util.exceptions.UserRegistrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {

    private final IKeycloakService keycloakService;

    private final IUserRepository userRepository;
    private final IProfilePictureRepository profilePictureRepository;
    private final IBillingInfoRepository billingInfoRepository;

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

        // TODO: Convert all these returns to use mappers instead
        UserBasicInfoResponse basicInfo = UserBasicInfoResponse.builder()
                .email(userRegisterRequest.getEmail())
                .firstName(userRegisterRequest.getFirstName())
                .lastName(userRegisterRequest.getLastName())
                .profilePictureUrl(null)
                .build();

        return UserResponse.builder()
                .basicInfo(basicInfo)
                .billingInfo(null)
                .build();
    }

    @Override
    public UserResponse getUserById(String userId) {
        UserEntity userEntity = this.userRepository.findByUserId(userId);
        BillingInfoEntity billingInfoEntity = this.billingInfoRepository.findByUserId(userId);

        // TODO: Get billing info as well and attach to user

        UserBasicInfoResponse basicInfo = UserBasicInfoResponse.builder()
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .profilePictureUrl(userEntity.getProfilePictureUrl())
                .build();

        return UserResponse.builder()
                .id(userId)
                .basicInfo(basicInfo)
                .billingInfo(null)
                .build();
    }

    @Override
    public UserBasicInfoResponse setUserBasicInfo(String userId,
                                                    UserBasicInfoRequest userBasicInfoRequest,
                                                    MultipartFile profilePic) throws UpdateUserException {
        try {
            var userEntity = this.userRepository.findByUserId(userId);

            // Update profile picture
            String profilePicId = null;
            String profilePicUrl = null;
            if(profilePic != null) {
                // Save new profile picture
                var fileExtension = StringUtils.getFilenameExtension(profilePic.getOriginalFilename());
                profilePicId = UUID.randomUUID() + "." + fileExtension;
                profilePicUrl = profilePictureRepository.uploadProfilePicture(profilePicId, profilePic);

                // Remove existing profile picture (silently fail as it may not exist)
                try {
                    profilePictureRepository.removeFile(userEntity.getProfilePictureId());
                } catch (Exception e) {
                    log.warn("Failed to remove profile picture with id: " + userEntity.getProfilePictureId());
                }
            }

            // Update user info table
            userEntity.setFirstName(userBasicInfoRequest.getFirstName());
            userEntity.setLastName(userBasicInfoRequest.getLastName());
//            userEntity.setEmail(userBasicInfoRequest.getEmail()); // TODO: Need to implement this in keycloak first
            if(profilePicUrl != null) {
                userEntity.setProfilePictureId(profilePicId);
                userEntity.setProfilePictureUrl(profilePicUrl);
            }

            this.userRepository.save(userEntity);

            return UserBasicInfoResponse.builder()
                    .email(userEntity.getEmail())
                    .firstName(userEntity.getFirstName())
                    .lastName(userEntity.getLastName())
                    .profilePictureUrl(userEntity.getProfilePictureUrl())
                    .build();

        } catch (Exception e) {
            throw new UpdateUserException("Failed to update user basic info");
        }
    }

    @Override
    public UserBillingInfoResponse setUserBillingInfo(String userId, UserBillingInfoRequest userBillingInfoRequest)
            throws UpdateUserException{
        try {
            BillingInfoEntity billingInfoEntity = BillingInfoEntity.builder()
                    .userId(userId)
                    .nameOnCard(userBillingInfoRequest.getNameOnCard())
                    .cardNumber(userBillingInfoRequest.getCardNumber())
                    .expirationDate(userBillingInfoRequest.getExpirationDate())
                    .cvv(userBillingInfoRequest.getCvv())
                    .build();

            this.billingInfoRepository.save(billingInfoEntity);

            return UserBillingInfoResponse.builder()
                    .nameOnCard(billingInfoEntity.getNameOnCard())
                    .cardNumber(billingInfoEntity.getCardNumber())
                    .expirationDate(billingInfoEntity.getExpirationDate())
                    .cvv(billingInfoEntity.getCvv())
                    .build();

        } catch (Exception e) {
            throw new UpdateUserException("Failed to save user's billing information");
        }

    }
}
