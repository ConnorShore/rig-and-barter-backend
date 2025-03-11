package com.rigandbarter.transactionservice.service.impl;

import com.rigandbarter.eventlibrary.components.RBEventProducer;
import com.rigandbarter.eventlibrary.components.RBEventProducerFactory;
import com.rigandbarter.eventlibrary.events.TransactionCompletedEvent;
import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.eventlibrary.events.TransactionInProgressEvent;
import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.core.models.TransactionResponse;
import com.rigandbarter.transactionservice.model.Transaction;
import com.rigandbarter.core.models.TransactionState;
import com.rigandbarter.transactionservice.repository.ITransactionRepository;
import com.rigandbarter.transactionservice.repository.mapper.TransactionMapper;
import com.rigandbarter.transactionservice.service.ITransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final RBEventProducer transactionCreatedProducer;
    private final RBEventProducer transactionInProgressProducer;
    private final RBEventProducer transactionCompletedProducer;
    private final String EVENT_SOURCE = "TransactionService";

    public TransactionServiceImpl(ITransactionRepository transactionRepository, RBEventProducerFactory rbEventProducerFactory) {
        this.transactionRepository = transactionRepository;

        transactionCreatedProducer = rbEventProducerFactory.createProducer(TransactionCreatedEvent.class);
        transactionInProgressProducer = rbEventProducerFactory.createProducer(TransactionInProgressEvent.class);
        transactionCompletedProducer = rbEventProducerFactory.createProducer(TransactionCompletedEvent.class);
    }

    @Override
    public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
        // Save the transaction to the database
        Transaction transaction = this.transactionRepository.save(TransactionMapper.dtoToEntity(transactionRequest));
        if(transaction == null)
            throw new InternalServerErrorException("Failed to create transaction in database");

        // Create and send TransactionCreatedEvent
        TransactionCreatedEvent event = TransactionCreatedEvent.builder()
                .id(UUID.randomUUID().toString())
                .userId(transaction.getSellerId())
                .sellerId(transaction.getSellerId())
                .buyerId(transaction.getBuyerId())
                .listingId(transaction.getListingId())
                .creationDate(LocalDateTime.now())
                .source(EVENT_SOURCE)
                .build();

        transactionCreatedProducer.send(event, this::handleFailedTransactionCreatedEventSend);
        return TransactionMapper.entityToDto(transaction);
    }

    @Override
    public TransactionResponse getTransactionForUser(String transactionId, String userId) {
        Transaction transaction = this.transactionRepository.findByUniqueId(transactionId);
        if(transaction == null)
            throw new NotFoundException("Transaction with id does not exist in db: " + transactionId);

        if(!userId.equals(transaction.getBuyerId()) && !userId.equals(transaction.getSellerId()))
            throw new NotAuthorizedException("User [" + userId + "] is no the buyer or seller of transaction" + transactionId);

        return TransactionMapper.entityToDto(transaction);
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
    public TransactionResponse acceptTransaction(String transactionId, Jwt principal) throws NotAuthorizedException {
        String userId = principal.getSubject();

        Transaction transaction = this.transactionRepository.findByUniqueId(transactionId);

        // Set buyer/seller accepted depending on which the user is
        if(userId.equals(transaction.getBuyerId()))
            transaction.setBuyerAccepted(true);
        else if(userId.equals(transaction.getSellerId()))
            transaction.setSellerAccepted(true);
        else
            throw new NotAuthorizedException("User is not associated with the transaction");

        // If both buyer and seller has accepted, the transaction is now in progress awaiting completing
        if(transaction.isBuyerAccepted() && transaction.isSellerAccepted()) {
            transaction.setState(TransactionState.IN_PROGRESS);

            TransactionInProgressEvent event = TransactionInProgressEvent.builder()
                    .id(UUID.randomUUID().toString())
                    .userId(transaction.getSellerId())
                    .authToken(principal.getTokenValue())
                    .transactionId(transaction.getUniqueId())
                    .buyerId(transaction.getBuyerId())
                    .sellerId(transaction.getSellerId())
                    .listingId(transaction.getListingId())
                    .creationDate(LocalDateTime.now())
                    .source(EVENT_SOURCE)
                    .build();

            transactionInProgressProducer.send(event, this::handleFailedTransactionInProgressEventSend);
        }

        this.transactionRepository.save(transaction);
        return TransactionMapper.entityToDto(transaction);
    }

    @Override
    public TransactionResponse completeTransaction(String transactionId, String paymentMethodId, boolean isManualTransaction, Jwt principal) {
        String userId = principal.getSubject();

        Transaction transaction = this.transactionRepository.findByUniqueId(transactionId);

        if(!userId.equals(transaction.getBuyerId()) && !userId.equals(transaction.getSellerId()))
            throw new NotAuthorizedException("User is not associated with the transaction");

        if(userId.equals(transaction.getBuyerId())) {
            transaction.setBuyerCompleted(true);
            transaction.setPaymentMethodId(paymentMethodId);
        }

        if(userId.equals(transaction.getSellerId()))
            transaction.setSellerCompleted(true);

        // If both buyer and seller have completed the transaction,
        // OR
        // It is a manual transaction and the seller has completed the transaction
        // send event to complete payment
        if((isManualTransaction || transaction.isBuyerCompleted()) && transaction.isSellerCompleted()) {
            transaction.setState(TransactionState.COMPLETED);
            transaction.setCompletionDate(LocalDateTime.now());

            TransactionCompletedEvent event = TransactionCompletedEvent.builder()
                    .id(UUID.randomUUID().toString())
                    .userId(transaction.getSellerId())
                    .transactionId(transactionId)
                    .paymentMethodId(transaction.getPaymentMethodId())
                    .buyerId(transaction.getBuyerId())
                    .sellerId(transaction.getSellerId())
                    .authToken(principal.getTokenValue())
                    .listingId(transaction.getListingId())
                    .creationDate(LocalDateTime.now())
                    .source(EVENT_SOURCE)
                    .build();

            transactionCompletedProducer.send(event, this::handleFailedTransactionCompletedEventSend);
        }

        transaction = this.transactionRepository.save(transaction);
        return TransactionMapper.entityToDto(transaction);
    }

    @Override
    public TransactionResponse cancelTransaction(String transactionId, String userId) {
        Transaction transaction = this.transactionRepository.findByUniqueId(transactionId);
        if(!userId.equals(transaction.getBuyerId()) && !userId.equals(transaction.getSellerId()))
            throw new NotAuthorizedException("User is not associated with the transaction");

        transaction.setState(TransactionState.CANCELLED);
        transactionRepository.save(transaction);

        //TODO: Send transaction cancelled event to delete messages associated with it

        return TransactionMapper.entityToDto(transaction);
    }

    @Override
    public void deleteTransaction(String id) {
        this.transactionRepository.deleteByUniqueId(id);
        this.transactionRepository.deleteByListingId(id);
    }

    @Override
    public List<TransactionResponse> getActiveTransactionsForListing(String listingId) {
        return this.transactionRepository.findAllByListingId(listingId).stream()
                .filter(transaction -> transaction.getCompletionDate() == null)
                .map(TransactionMapper::entityToDto)
                .toList();
    }

    /**
     * Handler for when a transaction created event fails to send
     * @param error The error from the sending failure
     */
    private Void handleFailedTransactionCreatedEventSend(String error) {
        log.error("Failed to send Transaction Created Event with error: " + error);
        return null;
    }

    /**
     * Handler for when a transaction created event fails to send
     * @param error The error from the sending failure
     */
    private Void handleFailedTransactionInProgressEventSend(String error) {
        log.error("Failed to send Transaction In Progress Event with error: " + error);
        return null;
    }

    /**
     * Handler for when a transaction created event fails to send
     * @param error The error from the sending failure
     */
    private Void handleFailedTransactionCompletedEventSend(String error) {
        log.error("Failed to send Transaction Completed Event with error: " + error);
        return null;
    }
}
