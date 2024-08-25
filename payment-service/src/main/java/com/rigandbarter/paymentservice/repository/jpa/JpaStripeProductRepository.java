package com.rigandbarter.paymentservice.repository.jpa;

import com.rigandbarter.paymentservice.model.StripeProduct;
import com.rigandbarter.paymentservice.repository.IStripeProductRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(value = "rb.storage.relational", havingValue = "mysql")
public interface JpaStripeProductRepository extends IStripeProductRepository, JpaRepository<StripeProduct, Long> {
}
