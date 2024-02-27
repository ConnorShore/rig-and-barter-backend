package com.rigandbarter.transactionservice.service;

import com.rigandbarter.eventlibrary.components.RBEventProducer;
import com.rigandbarter.eventlibrary.components.RBEventProducerFactory;
import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.model.Transaction;
import com.rigandbarter.transactionservice.model.TransactionState;
import com.rigandbarter.transactionservice.repository.ITransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class TransactionService {

    private final ITransactionRepository transactionRepository;
    private final RBEventProducer transactionCreatedProducer;

    public TransactionService(
            ITransactionRepository transactionRepository,
            RBEventProducerFactory rbEventProducerFactory) {
        this.transactionRepository = transactionRepository;

        transactionCreatedProducer = rbEventProducerFactory.createProducer(TransactionCreatedEvent.class);
    }

    public Transaction createTransaction(TransactionRequest transactionRequest, String buyerId) {
        // Save the transaction to the database
        Transaction transaction = this.transactionRepository.save(
            Transaction.builder()
                .uniqueId(UUID.randomUUID().toString())
                .buyerId(buyerId)
                .sellerId(transactionRequest.getSellerId())
                .listingId(transactionRequest.getListingId())
                .title(transactionRequest.getTitle())
                .creationDate(LocalDateTime.now())
                .state(TransactionState.REQUESTED)
                .build()
        );

        // Create and send TransactionCreatedEvent
        TransactionCreatedEvent event = TransactionCreatedEvent.builder()
                .id(UUID.randomUUID().toString())
                .userId(transaction.getSellerId())
                .buyerId(buyerId)
                .listingId(transaction.getListingId())
                .creationDate(LocalDateTime.now())
                .source("TransactionService")
                .build();

        transactionCreatedProducer.send(event);

        return transaction;
    }

    public void createEvent() {
        TransactionCreatedEvent event = TransactionCreatedEvent.builder()
                .id(UUID.randomUUID().toString())
                .userId("userId")
                .buyerId("buyerId")
                .listingId("listingId")
                .creationDate(LocalDateTime.now())
                .source("TransactionService")
                .build();

        transactionCreatedProducer.send(event);
    }
}
