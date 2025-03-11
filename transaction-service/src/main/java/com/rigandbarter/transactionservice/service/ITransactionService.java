package com.rigandbarter.transactionservice.service;

import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.core.models.TransactionResponse;
import com.rigandbarter.transactionservice.model.Transaction;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface ITransactionService {

    /**
     * Creates and processes the transaction
     * @param transactionRequest The transaction to process
     * @return The transaction if everything was handled
     */
    TransactionResponse createTransaction(TransactionRequest transactionRequest);

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
     * @param isManualTransaction True if the transaction is being done in person between buyer and seller
     * @param principal The auth object of the user
     * @return The updated transaction
     */
    TransactionResponse completeTransaction(String transactionId, String paymentMethodId, boolean isManualTransaction, Jwt principal);

    /**
     * Marks the transaction as completed
     * @param transactionId The id of the transaction to complete
     * @param userId The id of the user
     * @return The updated transaction
     */
    TransactionResponse cancelTransaction(String transactionId, String userId);

    /**
     * Deletes the transaction based on id (Maybe listingId as well)
     * @param id The id within the transaction to be deleted
     */
    void deleteTransaction(String id);

    /**
     * Gets active transactions for the given listing
     * @param listingId The id of the listing to get transactions for
     * @return The list of active transactions for the given listing
     */
    List<TransactionResponse> getActiveTransactionsForListing(String listingId);
}
