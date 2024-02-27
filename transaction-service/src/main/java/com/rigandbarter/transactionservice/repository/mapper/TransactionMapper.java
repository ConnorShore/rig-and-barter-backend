package com.rigandbarter.transactionservice.repository.mapper;

import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.model.Transaction;
import com.rigandbarter.transactionservice.model.TransactionState;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionMapper {
    public static Transaction fromRequestDto(TransactionRequest transactionRequest) {
        return Transaction.builder()
                .uniqueId(UUID.randomUUID().toString())
                .buyerId(transactionRequest.getBuyerId())
                .sellerId(transactionRequest.getSellerId())
                .listingId(transactionRequest.getListingId())
                .title(transactionRequest.getTitle())
                .creationDate(LocalDateTime.now())
                .state(TransactionState.REQUESTED)
                .build();
    }
}
