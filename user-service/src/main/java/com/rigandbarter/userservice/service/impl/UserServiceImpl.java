package com.rigandbarter.userservice.service.impl;

import com.rigandbarter.core.models.StripeCustomerResponse;
import com.rigandbarter.core.models.UserBasicInfo;
import com.rigandbarter.core.models.UserBasicInfoResponse;
import com.rigandbarter.core.models.UserResponse;
import com.rigandbarter.eventlibrary.components.RBEventProducer;
import com.rigandbarter.eventlibrary.components.RBEventProducerFactory;
import com.rigandbarter.eventlibrary.events.StripeCustomerCreatedEvent;
import com.rigandbarter.eventlibrary.events.UserCreatedEvent;
import com.rigandbarter.eventlibrary.events.UserDeletedEvent;
import com.rigandbarter.userservice.client.PaymentServiceClient;
import com.rigandbarter.userservice.dto.*;
import com.rigandbarter.userservice.mapper.UserMapper;
import com.rigandbarter.userservice.model.UserEntity;
import com.rigandbarter.userservice.repository.object.IProfilePictureRepository;
import com.rigandbarter.userservice.repository.relational.IUserRepository;
import com.rigandbarter.userservice.service.IKeycloakService;
import com.rigandbarter.userservice.service.IUserService;
import com.rigandbarter.userservice.util.exceptions.UpdateUserException;
import com.rigandbarter.userservice.util.exceptions.UserRegistrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IProfilePictureRepository profilePictureRepository;

    private final IKeycloakService keycloakService;

    private final PaymentServiceClient paymentServiceClient;

    private final RBEventProducer userCreatedProducer;;
    private final RBEventProducer userDeletedProducer;

    private final String EVENT_SOURCE = "UserService";

    public UserServiceImpl(IKeycloakService keycloakService,
                           IUserRepository userRepository,
                           IProfilePictureRepository profilePictureRepository,
                           RBEventProducerFactory rbEventProducerFactory,
                           PaymentServiceClient paymentServiceClient)
    {
        this.keycloakService = keycloakService;

        this.userRepository = userRepository;
        this.profilePictureRepository = profilePictureRepository;

        this.userCreatedProducer = rbEventProducerFactory.createProducer(UserCreatedEvent.class);
        this.userDeletedProducer = rbEventProducerFactory.createProducer(UserDeletedEvent.class);

        this.paymentServiceClient = paymentServiceClient;
    }

    @Override
    public UserResponse registerUser(UserRegisterRequest userRegisterRequest) throws UserRegistrationException {
        // Register user with keycloak
        String userId = this.keycloakService.registerUser(userRegisterRequest);
        if(userId == null) {
            // TODO: I think this throws a 401, but should be a 500
            throw new UserRegistrationException("Failed to register user with keycloak: " + userRegisterRequest.getEmail());
        }

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
                .stripeInfo(null)
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

    /**
     * Sends the user deleted event
     * @param userId The id of the user whose resources to delete
     */
    private void sendUserDeletedEvent(String userId) {
        UserDeletedEvent event = UserDeletedEvent.builder()
                .userId(userId)
                .id(UUID.randomUUID().toString())
                .source(EVENT_SOURCE)
                .creationDate(LocalDateTime.now())
                .build();

        userDeletedProducer.send(event, this::handleFailedUserCreatedEvent);
    }

    @Override
    public UserResponse getUserById(String userId, Jwt princiapl) {
        UserEntity userEntity = this.userRepository.findByUserId(userId);

        UserBasicInfoResponse basicInfo = UserBasicInfoResponse.builder()
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .profilePictureUrl(userEntity.getProfilePictureUrl())
                .build();

        // Get stripe customer info via web call to payment service
        StripeCustomerResponse stripeCustomerInfo = null;
        if(userEntity.getStripeCustomerId() != null) {
            stripeCustomerInfo = paymentServiceClient.getPaymentProfile("Bearer " + princiapl.getTokenValue());

            if (stripeCustomerInfo == null) {
                String msg = "Failed to retrieve stripe customer info from payment service: " + userEntity.getStripeCustomerId();
                throw new NotFoundException(msg);
            }

            if(!stripeCustomerInfo.getStripeId().equals(userEntity.getStripeCustomerId())) {
                throw new NotFoundException("Stripe ID Mismatch on user entity and stripe customer");
            }
        }

        return UserResponse.builder()
                .id(userId)
                .basicInfo(basicInfo)
                .stripeInfo(stripeCustomerInfo)
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

    @Override
    public void deleteUserById(String userId) {
        log.info("Deleting user with id: " + userId);

        var userEntity = this.userRepository.findByUserId(userId);
        this.keycloakService.deleteUser(userEntity.getUserId());
        this.userRepository.delete(userEntity);

        log.info("User [{}] removed from db and keycloak. Sending user deleted event", userId);

        sendUserDeletedEvent(userId);
    }

    @Override
    public void setUserStripeCustomerInfo(StripeCustomerCreatedEvent stripeCustomerCreatedEvent) throws UpdateUserException {
        log.info("Setting user's stripe customer info");
        var userEntity = this.userRepository.findByUserId(stripeCustomerCreatedEvent.getUserId());

        if(userEntity == null)
            throw new UpdateUserException("Failed to find user with id: " + stripeCustomerCreatedEvent.getUserId());

        userEntity.setStripeCustomerId(stripeCustomerCreatedEvent.getStripeCustomerId());
        this.userRepository.save(userEntity);

        log.info("Successfully set user's stripe customer info");
    }

    @Override
    public boolean isUserVerified(String userId, Jwt principal) {
        var userEntity = userRepository.findByUserId(userId);
        if (userEntity.getStripeCustomerId() == null)
            return false;

        // Get stripe customer info via web call to payment service
        StripeCustomerResponse stripeCustomerInfo = paymentServiceClient.getPaymentProfile("Bearer " + principal.getTokenValue());
        if (stripeCustomerInfo == null) {
            String msg = "Failed to retrieve stripe customer info from payment service: " + userEntity.getStripeCustomerId();
            throw new NotFoundException(msg);
        }

        if (!stripeCustomerInfo.getStripeId().equals(userEntity.getStripeCustomerId())) {
            throw new NotFoundException("Stripe ID Mismatch on user entity and stripe customer");
        }

        return stripeCustomerInfo.isVerified();
    }

    /**
     * Handler for when a transaction created event fails to send
     * @param error The error from the sending failure
     */
    private Void handleFailedUserCreatedEvent(String error) {
        log.error("Failed to send User Created Event with error: " + error);
        return null;
    }
}
