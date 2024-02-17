package com.rigandbarter.transactionservice.repository;

import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
}
