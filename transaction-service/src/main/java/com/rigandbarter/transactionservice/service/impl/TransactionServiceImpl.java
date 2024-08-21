package com.rigandbarter.transactionservice.service.impl;

import com.rigandbarter.eventlibrary.components.RBEventProducer;
import com.rigandbarter.eventlibrary.components.RBEventProducerFactory;
import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.dto.TransactionResponse;
import com.rigandbarter.transactionservice.model.Transaction;
import com.rigandbarter.transactionservice.model.TransactionState;
import com.rigandbarter.transactionservice.repository.ITransactionRepository;
import com.rigandbarter.transactionservice.repository.mapper.TransactionMapper;
import com.rigandbarter.transactionservice.service.ITransactionService;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Transaction transaction = this.transactionRepository.save(TransactionMapper.dtoToEntity(transactionRequest));
        if(transaction == null)
            throw new InternalServerErrorException("Failed to create transaciton in database");

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

    @Override
    public List<TransactionResponse> getAllTransactionsForUser(String userId) {
        List<Transaction> transactions = this.transactionRepository.findAllBySellerId(userId);
        transactions.addAll(this.transactionRepository.findAllByBuyerId(userId));
        return transactions.stream()
                .map(TransactionMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void acceptTransaction(String transactionId, String userId) throws NotAuthorizedException {
        Transaction transaction = this.transactionRepository.findByUniqueId(transactionId);

        if(userId.equals(transaction.getBuyerId()))
            transaction.setBuyerAccepted(true);
        else if(userId.equals(transaction.getSellerId()))
            transaction.setSellerAccepted(true);
        else
            throw new NotAuthorizedException("User is not associated with the transaction");

        this.transactionRepository.save(transaction);
    }

    @Override
    public void completeTransaction(String transactionId, String userId) {
        Transaction transaction = this.transactionRepository.findByUniqueId(transactionId);

        if(!userId.equals(transaction.getBuyerId()) && !userId.equals(transaction.getSellerId()))
            throw new NotAuthorizedException("User is not associated with the transaction");


        transaction.setState(TransactionState.COMPLETED);
        transaction.setCompletionDate(LocalDateTime.now());

        // TODO: Send out an event that the transaction is completed
        //  Should be consumed by the payment service so it can process payment stuff
        //  Also send out notifications to buyer and seller (front end, email, etc)

        this.transactionRepository.save(transaction);
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
