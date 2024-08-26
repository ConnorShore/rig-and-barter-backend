package com.rigandbarter.transactionservice.repository;

import com.rigandbarter.transactionservice.model.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ITransactionRepository {

    /**
     * Saves the transaction to the database
     * @param transaction The transaction to save
     * @return The transaction if saved, null otherwise
     */
    Transaction save(Transaction transaction);

    /**
     * Finds the transaction with specified id
     * @param transactionId The id of the transaction to find
     * @return The transacitno if found
     */
    Transaction findByUniqueId(String transactionId);

    /**
     * Gets all selling transactions for the user
     * @param sellerId The id of the selling user to get transactions for
     * @return All transactinos for the user
     */
    List<Transaction> findAllBySellerId(String sellerId);

    /**
     * Gets all buing transactions for the user
     * @param buyerId The id of the buying user to get transactions for
     * @return All transactinos for the user
     */
    List<Transaction> findAllByBuyerId(String buyerId);

    /**
     * Deletes the transaction with the given id
     * @param transactionId The id of the transaction to delete
     */
    void deleteByUniqueId(String transactionId);

    /**
     * Deletes the transaction with the associated listing id
     * @param listingId The id of the listing in the transaction to delete
     */
    void deleteByListingId(String listingId);
}
