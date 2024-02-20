package com.rigandbarter.transactionservice.service;

import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.model.Transaction;
import com.rigandbarter.transactionservice.model.TransactionState;
import com.rigandbarter.transactionservice.repository.ITransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final ITransactionRepository transactionRepository;
    public Transaction createTransaction(TransactionRequest transactionRequest, String buyerId) {
        Transaction transaction = Transaction.builder()
                .uniqueId(UUID.randomUUID().toString())
                .buyerId(buyerId)
                .sellerId(transactionRequest.getSellerId())
                .listingId(transactionRequest.getListingId())
                .title(transactionRequest.getTitle())
                .creationDate(LocalDateTime.now())
                .state(TransactionState.REQUESTED)
                .build();

        return this.transactionRepository.save(transaction);
    }
}
