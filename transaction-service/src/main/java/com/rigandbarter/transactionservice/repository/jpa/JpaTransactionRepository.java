package com.rigandbarter.transactionservice.repository.jpa;

import com.rigandbarter.transactionservice.model.Transaction;
import com.rigandbarter.transactionservice.repository.ITransactionRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(value = "rb.storage.relational", havingValue = "mysql")
public interface JpaTransactionRepository extends ITransactionRepository, JpaRepository<Transaction, Long> {
}
