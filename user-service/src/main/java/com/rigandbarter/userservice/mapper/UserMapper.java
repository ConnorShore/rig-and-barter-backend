package com.rigandbarter.userservice.mapper;

import com.rigandbarter.core.models.UserBasicInfo;
import com.rigandbarter.core.models.UserBillingInfo;
import com.rigandbarter.userservice.dto.UserBasicInfoRequest;
import com.rigandbarter.userservice.model.BillingInfoEntity;
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

    /**
     * Maps the user entity to the basic info object
     * @param entity The user entity to map
     * @return The user basic info object
     */
    public static UserBasicInfo entityToBasicInfo(UserEntity entity) {
        return UserBasicInfo.builder()
                .userId(entity.getUserId())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .profilePictureUrl(entity.getProfilePictureUrl())
                .build();
    }

    /**
     * Maps the billing info entity to the billing info object
     * @param entity The billing info entity to map
     * @return The created User Billing Info object
     */
    public static UserBillingInfo entityToBillingInfo(BillingInfoEntity entity) {
        return UserBillingInfo.builder()
                .userId(entity.getUserId())
                .stripeCardToken(entity.getStripeCardToken())
//                .nameOnCard(entity.getNameOnCard())
//                .cardNumber(entity.getCardNumber())
//                .expirationDate(entity.getExpirationDate())
//                .cvv(entity.getCvv())
                .build();
    }
}
