package com.rigandbarter.transactionservice.repository;

import com.rigandbarter.transactionservice.model.Transaction;

public interface ITransactionRepository {
    Transaction save(Transaction transaction);
}
