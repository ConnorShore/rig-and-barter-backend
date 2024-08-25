package com.rigandbarter.paymentservice.mapper;

import com.rigandbarter.core.models.StripeCustomerResponse;
import com.rigandbarter.core.models.StripePaymentMethodResponse;
import com.rigandbarter.paymentservice.model.StripeCustomer;
import com.rigandbarter.paymentservice.model.StripePaymentMethod;

public class StripeMapper {

    public static StripePaymentMethodResponse paymentEntityToDto(StripePaymentMethod entity) {
        return StripePaymentMethodResponse.builder()
                .stripePaymentId(entity.getStripePaymentId())
                .cardToken(entity.getCardToken())
                .nickname(entity.getNickname())
                .last4Digits(entity.getLast4Digits())
                .expirationMonth(entity.getExpirationMonth())
                .expirationYear(entity.getExpirationYear())
                .build();
    }

    public static StripeCustomerResponse customerEntityToDto(StripeCustomer entity) {
        return StripeCustomerResponse.builder()
                .userId(entity.getUserId())
                .stripeId(entity.getStripeId())
                .accountId(entity.getAccountId())
                .verified(entity.isVerified())
                .paymentMethods(
                        entity.getPaymentMethods()
                                .stream()
                                .map(StripeMapper::paymentEntityToDto)
                                .toList()
                )
                .build();
    }
}
