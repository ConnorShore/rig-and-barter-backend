package com.rigandbarter.paymentservice.repository.jpa;

import com.rigandbarter.paymentservice.model.StripePaymentMethod;
import com.rigandbarter.paymentservice.repository.IStripePaymentMethodRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(value = "rb.storage.relational", havingValue = "mysql")
public interface JpaStripePaymentMethodRepository extends IStripePaymentMethodRepository, JpaRepository<StripePaymentMethod, Long> {
}
