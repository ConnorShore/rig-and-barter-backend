package com.rigandbarter.userservice.repository.relational.jpa;

import com.rigandbarter.userservice.model.BillingInfoEntity;
import com.rigandbarter.userservice.repository.relational.IBillingInfoRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(value = "rb.storage.relational", havingValue = "mysql")
public interface JpaBillingInfoRepositoryImpl extends IBillingInfoRepository, JpaRepository<BillingInfoEntity, Long> {
}
