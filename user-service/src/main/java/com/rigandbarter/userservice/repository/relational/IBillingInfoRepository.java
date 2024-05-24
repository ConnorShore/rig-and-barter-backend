package com.rigandbarter.userservice.repository.relational;

import com.rigandbarter.userservice.model.BillingInfoEntity;

public interface IBillingInfoRepository {

    /**
     * Saves the user's billing information
     * @param billingInfoEntity The billing info to save
     * @return The saved billing information
     */
    BillingInfoEntity save(BillingInfoEntity billingInfoEntity);

    /**
     * Get's the billing info for the user with the specified uid
     * @param userId The uid of the user to get billing info for
     * @return The billing info of the user with the specified uid
     */
    BillingInfoEntity findByUserId(String userId);
}
