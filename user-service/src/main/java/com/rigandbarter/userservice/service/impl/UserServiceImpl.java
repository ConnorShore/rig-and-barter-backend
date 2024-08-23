package com.rigandbarter.userservice.service.impl;

import com.rigandbarter.core.models.UserBasicInfo;
import com.rigandbarter.core.models.UserBillingInfo;
import com.rigandbarter.eventlibrary.components.RBEventProducer;
import com.rigandbarter.eventlibrary.components.RBEventProducerFactory;
import com.rigandbarter.eventlibrary.events.BillingInfoUpdatedEvent;
import com.rigandbarter.eventlibrary.events.UserCreatedEvent;
import com.rigandbarter.userservice.dto.*;
import com.rigandbarter.userservice.mapper.UserMapper;
import com.rigandbarter.userservice.model.BillingInfoEntity;
import com.rigandbarter.userservice.model.UserEntity;
import com.rigandbarter.userservice.repository.file.IProfilePictureRepository;
import com.rigandbarter.userservice.repository.relational.IBillingInfoRepository;
import com.rigandbarter.userservice.repository.relational.IUserRepository;
import com.rigandbarter.userservice.service.IKeycloakService;
import com.rigandbarter.userservice.service.IUserService;
import com.rigandbarter.userservice.util.exceptions.UpdateUserException;
import com.rigandbarter.userservice.util.exceptions.UserRegistrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    private final IKeycloakService keycloakService;
    private final IUserRepository userRepository;
    private final IProfilePictureRepository profilePictureRepository;
    private final IBillingInfoRepository billingInfoRepository;
    private final RBEventProducer userCreatedProducer;;
    private final RBEventProducer billingInfoUpdatedProducer;
    private final String EVENT_SOURCE = "UserService";

    public UserServiceImpl(IKeycloakService keycloakService,
                           IUserRepository userRepository,
                           IProfilePictureRepository profilePictureRepository,
                           IBillingInfoRepository billingInfoRepository,
                           RBEventProducerFactory rbEventProducerFactory)
    {
        this.keycloakService = keycloakService;
        this.userRepository = userRepository;
        this.profilePictureRepository = profilePictureRepository;
        this.billingInfoRepository = billingInfoRepository;

        this.userCreatedProducer = rbEventProducerFactory.createProducer(UserCreatedEvent.class);
        this.billingInfoUpdatedProducer = rbEventProducerFactory.createProducer(BillingInfoUpdatedEvent.class);
    }

    @Override
    public UserResponse registerUser(UserRegisterRequest userRegisterRequest) throws UserRegistrationException {
        // Register user with keycloak
        String userId = this.keycloakService.registerUser(userRegisterRequest);
        if(userId == null)
            throw new UserRegistrationException("Failed to register user with keycloak: " + userRegisterRequest.getEmail());

        // Add user to database
        UserEntity userEntity = null;
        try {
            userEntity = UserEntity.builder()
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

        // Send the user created event
        sendUserCreatedEvent(userEntity);

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


    /**
     * Sends the created user event
     * @param user The user entity to send the event for
     */
    private void sendUserCreatedEvent(UserEntity user) {
        UserBasicInfo userInfo = UserMapper.entityToBasicInfo(user);
        UserCreatedEvent event = UserCreatedEvent.builder()
                .userInfo(userInfo)
                .id(UUID.randomUUID().toString())
                .source(EVENT_SOURCE)
                .creationDate(LocalDateTime.now())
                .build();

        userCreatedProducer.send(event, this::handleFailedUserCreatedEvent);
    }

    @Override
    public UserResponse getUserById(String userId) {
        UserEntity userEntity = this.userRepository.findByUserId(userId);
        BillingInfoEntity billingInfoEntity = this.billingInfoRepository.findByUserId(userId);

        UserBasicInfoResponse basicInfo = UserBasicInfoResponse.builder()
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .profilePictureUrl(userEntity.getProfilePictureUrl())
                .build();

        UserBillingInfoResponse billingInfo = null;
//        if(billingInfoEntity != null) {
//            billingInfo = UserBillingInfoResponse.builder()
//                    .nameOnCard(billingInfoEntity.getNameOnCard())
//                    .cardNumber(billingInfoEntity.getCardNumber())
//                    .expirationDate(billingInfoEntity.getExpirationDate())
//                    .cvv(billingInfoEntity.getCvv())
//                    .build();
//        }

        return UserResponse.builder()
                .id(userId)
                .basicInfo(basicInfo)
                .billingInfo(billingInfo)
                .build();
    }

    @Override
    public UserBasicInfoResponse getUserBasicInfoById(String userId) {
        UserEntity userEntity = this.userRepository.findByUserId(userId);
        return UserBasicInfoResponse.builder()
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .profilePictureUrl(userEntity.getProfilePictureUrl())
                .build();
    }

    @Override
    public UserBasicInfoResponse setUserBasicInfo(String userId,
                                                  UserBasicInfoRequest userBasicInfoRequest,
                                                  MultipartFile profilePic) throws UpdateUserException {
        try {
            // Create user entity in db
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

    /**
     * TODO: Redesign user billing info.  Since we have payment service which is responsible for all payment stuff
     *  we may want billing info to only reside there and then the user entity has a handle on their stripe customerId
     *  Also make it so user's can have multiple payment methods
     */

    @Override
    public UserBillingInfoResponse setUserBillingInfo(String userId, UserBillingInfoRequest userBillingInfoRequest)
            throws UpdateUserException{
        try {
            // Add the billing info to the db
            BillingInfoEntity billingInfoEntity = this.billingInfoRepository.findByUserId(userId);
            if(billingInfoEntity == null) {
                billingInfoEntity = BillingInfoEntity.builder()
                        .userId(userId)
                        .nameOnCard(userBillingInfoRequest.getNameOnCard())
                        .stripeCardToken(userBillingInfoRequest.getStripeCardToken())
                        .build();
            } else {
                billingInfoEntity.setNameOnCard(userBillingInfoRequest.getNameOnCard());
                billingInfoEntity.setStripeCardToken(userBillingInfoRequest.getStripeCardToken());
            }

            this.billingInfoRepository.save(billingInfoEntity);

            // Send billing info updated event
            sendBillingInfoUpdatedEvent(billingInfoEntity, userId);

            return UserBillingInfoResponse.builder()
                    .nameOnCard(billingInfoEntity.getNameOnCard())
                    .stripeCardToken(userBillingInfoRequest.getStripeCardToken())
                    .build();

        } catch (Exception e) {
            throw new UpdateUserException("Failed to save user's billing information");
        }
    }

    /**
     * Sends the billing info updated event
     * @param billingInfoEntity The billing info entity to send the event for
     * @param userId The id of the user whose billing info this is
     */
    private void sendBillingInfoUpdatedEvent(BillingInfoEntity billingInfoEntity, String userId) {
        UserBillingInfo billingInfo = UserMapper.entityToBillingInfo(billingInfoEntity);
        BillingInfoUpdatedEvent event = BillingInfoUpdatedEvent.builder()
                .billingInfo(billingInfo)
                .userId(userId)
                .id(UUID.randomUUID().toString())
                .source(EVENT_SOURCE)
                .creationDate(LocalDateTime.now())
                .build();

        billingInfoUpdatedProducer.send(event, this::handleFailedBillingInfoUpdatedEvent);
    }

    /**
     * Handler for when a transaction created event fails to send
     * @param error The error from the sending failure
     */
    private Void handleFailedUserCreatedEvent(String error) {
        log.error("Failed to send User Created Event with error: " + error);
        return null;
    }

    /**
     * Handler for when a transaction created event fails to send
     * @param error The error from the sending failure
     */
    private Void handleFailedBillingInfoUpdatedEvent(String error) {
        log.error("Failed to send Billing Info Updated Event with error: " + error);
        return null;
    }
}
