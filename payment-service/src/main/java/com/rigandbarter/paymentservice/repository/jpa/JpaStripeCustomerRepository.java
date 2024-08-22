package com.rigandbarter.paymentservice.repository.jpa;

import com.rigandbarter.paymentservice.model.StripeCustomer;
import com.rigandbarter.paymentservice.repository.IStripeCustomerRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(value = "rb.storage.relational", havingValue = "mysql")
public interface JpaStripeCustomerRepository extends IStripeCustomerRepository, JpaRepository<StripeCustomer, Long> {
}
