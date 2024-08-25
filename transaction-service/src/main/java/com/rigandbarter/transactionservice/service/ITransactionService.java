package com.rigandbarter.transactionservice.service;

import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.dto.TransactionResponse;
import com.rigandbarter.transactionservice.model.Transaction;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface ITransactionService {

    /**
     * Creates and processes the transaction
     * @param transactionRequest The transaction to process
     * @return The transaction if everything was handled
     */
    Transaction createTransaction(TransactionRequest transactionRequest);

    /**
     * Gets the specified transaction for the user
     * @param transactionId The id of the transaction to retrieve
     * @param userId The id of the user who is requesting the transaction
     * @return The transaction
     */
    TransactionResponse getTransactionForUser(String transactionId, String userId);

    /**
     * Gets all active transactions for the user
     * @param userId The id of the user to get transactions for
     * @return All active transactions for the user
     */
    List<TransactionResponse> getAllTransactionsForUser(String userId);

    /**
     * Marks the transaction as accepted by the buyer/seller depending what the user is
     * @param transactionId The id of the transaction to accept
     * @param principal The user auth principal
     * @return The updated transaction
     */
    TransactionResponse acceptTransaction(String transactionId, Jwt principal);

    /**
     * Marks the transaction as completed
     * @param transactionId The id of the transaction to complete
     * @param paymentMethodId The id of the payment method to use (null if the seller)
     * @param principal The auth object of the user
     * @return The updated transaction
     */
    TransactionResponse completeTransaction(String transactionId, String paymentMethodId, Jwt principal);

    /**
     * Sets the transaction id for the setup intent
     * @param transactionId
     * @param setupIntentId
     */
    void setTransactionSetupIntentId(String transactionId, String setupIntentId);
}
