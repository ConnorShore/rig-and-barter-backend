package com.rigandbarter.transactionservice.repository;

import com.rigandbarter.transactionservice.model.Transaction;

public interface ITransactionRepository {

    /**
     * Saves the transaction to the database
     * @param transaction The transaction to save
     * @return The transaction if saved, null otherwise
     */
    Transaction save(Transaction transaction);
}
