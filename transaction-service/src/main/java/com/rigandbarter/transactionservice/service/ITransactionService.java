package com.rigandbarter.transactionservice.service;

import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.dto.TransactionResponse;
import com.rigandbarter.transactionservice.model.Transaction;

import java.util.List;

public interface ITransactionService {

    /**
     * Creates and processes the transaction
     * @param transactionRequest The transaction to process
     * @return The transaction if everything was handled
     */
    Transaction createTransaction(TransactionRequest transactionRequest);

    /**
     * Gets all active transactions for the user
     * @param userId The id of the user to get transactions for
     * @return All active transactions for the user
     */
    List<TransactionResponse> getAllTransactionsForUser(String userId);

    /**
     * Marks the transaction as accepted by the buyer/seller depending what the user is
     * @param transactionId The id of the transaction to accept
     * @param userId The id of the user who is accepting the transaction
     */
    void acceptTransaction(String transactionId, String userId);

    /**
     * Marks the transaction as completed
     * @param transactionId The id of the transaction to complete
     */
    void completeTransaction(String transactionId, String userId);
}
