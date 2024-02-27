package com.rigandbarter.transactionservice.service;

import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.model.Transaction;

public interface ITransactionService {

    /**
     * Creates and processes the transaction
     * @param transactionRequest The transaction to process
     * @return The transaction if everything was handled
     */
    Transaction createTransaction(TransactionRequest transactionRequest);
}
