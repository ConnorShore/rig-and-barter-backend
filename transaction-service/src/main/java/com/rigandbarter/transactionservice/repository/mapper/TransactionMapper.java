package com.rigandbarter.transactionservice.repository.mapper;

import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.dto.TransactionResponse;
import com.rigandbarter.transactionservice.model.Transaction;
import com.rigandbarter.transactionservice.model.TransactionState;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionMapper {

    /**
     * Converts TransactionRequest to Transaction entity
     * @param transactionRequest The transaction request to convert
     * @return The converted Transaction entity
     */
    public static Transaction dtoToEntity(TransactionRequest transactionRequest) {
        return Transaction.builder()
                .uniqueId(UUID.randomUUID().toString())
                .buyerId(transactionRequest.getBuyerId())
                .sellerId(transactionRequest.getSellerId())
                .listingId(transactionRequest.getListingId())
                .title(transactionRequest.getTitle())
                .creationDate(LocalDateTime.now())
                .state(TransactionState.REQUESTED)
                .buyerAccepted(false)
                .sellerAccepted(false)
                .build();
    }

    /**
     * Converts TransactionRequest to Transaction entity
     * @param transaction The transaction request to convert
     * @return The converted Transaction entity
     */
    public static TransactionResponse entityToDto(Transaction transaction) {
        return TransactionResponse.builder()
                .uniqueId(transaction.getUniqueId())
                .buyerId(transaction.getBuyerId())
                .sellerId(transaction.getSellerId())
                .listingId(transaction.getListingId())
                .title(transaction.getTitle())
                .creationDate(transaction.getCreationDate())
                .completionDate(transaction.getCompletionDate())
                .buyerAccepted(transaction.isBuyerAccepted())
                .sellerAccepted(transaction.isSellerAccepted())
                .state(transaction.getState())
                .build();
    }
}
