package com.rigandbarter.transactionservice.service.impl;

import com.rigandbarter.eventlibrary.components.RBEventProducer;
import com.rigandbarter.eventlibrary.components.RBEventProducerFactory;
import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.model.Transaction;
import com.rigandbarter.transactionservice.repository.ITransactionRepository;
import com.rigandbarter.transactionservice.repository.mapper.TransactionMapper;
import com.rigandbarter.transactionservice.service.ITransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final RBEventProducer transactionCreatedProducer;
    private final String EVENT_SOURCE = "TransactionService";

    public TransactionServiceImpl(
            ITransactionRepository transactionRepository,
            RBEventProducerFactory rbEventProducerFactory) {
        this.transactionRepository = transactionRepository;

        transactionCreatedProducer = rbEventProducerFactory.createProducer(TransactionCreatedEvent.class);
    }

    @Override
    public Transaction createTransaction(TransactionRequest transactionRequest) {
        // Save the transaction to the database
        Transaction transaction = this.transactionRepository.save(TransactionMapper.fromRequestDto(transactionRequest));

        // Create and send TransactionCreatedEvent
        TransactionCreatedEvent event = TransactionCreatedEvent.builder()
                .id(UUID.randomUUID().toString())
                .userId(transaction.getSellerId())
                .buyerId(transaction.getBuyerId())
                .listingId(transaction.getListingId())
                .creationDate(LocalDateTime.now())
                .source(EVENT_SOURCE)
                .build();

        transactionCreatedProducer.send(event, this::handleFailedTransactionCreatedEventSend);

        return transaction;
    }

    /**
     * Handler for when a transaction created event fails to send
     * @param error The error from the sending failure
     */
    private Void handleFailedTransactionCreatedEventSend(String error) {
        log.error("Failed to send Transaction Created Event with error: " + error);
        return null;
    }
}
