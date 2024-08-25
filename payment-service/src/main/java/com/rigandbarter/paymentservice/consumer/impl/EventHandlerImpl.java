package com.rigandbarter.paymentservice.consumer.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.components.RBEventConsumer;
import com.rigandbarter.eventlibrary.components.RBEventConsumerFactory;
import com.rigandbarter.eventlibrary.events.TransactionCompletedEvent;
import com.rigandbarter.eventlibrary.events.TransactionInProgressEvent;
import com.rigandbarter.eventlibrary.events.UserCreatedEvent;
import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.eventlibrary.model.RBEventHandler;
import com.rigandbarter.paymentservice.consumer.IEventHandler;
import com.rigandbarter.paymentservice.service.IEventHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventHandlerImpl extends RBEventHandler implements IEventHandler {
    private final IEventHandlerService eventHandlerService;
    private RBEventConsumer userCreatedConsumer;
//    private RBEventConsumer transactionInProgressConsumer;
    private RBEventConsumer transactionCompletedConsumer;

    public EventHandlerImpl(RBEventConsumerFactory rbEventConsumerFactory, IEventHandlerService eventHandlerService) {
        super(rbEventConsumerFactory);
        this.eventHandlerService = eventHandlerService;
    }

    @Override
    public void initializeConsumers() {
        log.info("Initializing consumers");

        userCreatedConsumer = rbEventConsumerFactory.createConsumer(UserCreatedEvent.class, this::handleUserCreatedEvent);
//        transactionInProgressConsumer = rbEventConsumerFactory.createConsumer(TransactionInProgressEvent.class, this::handleTransactionInProgressEvent);
        transactionCompletedConsumer = rbEventConsumerFactory.createConsumer(TransactionCompletedEvent.class, this::handleTransactionCompletedEvent);
    }

    @Override
    public void startConsumers() {
        log.info("Starting consumers");

        userCreatedConsumer.start();
//        transactionInProgressConsumer.start();
        transactionCompletedConsumer.start();
    }

    @Override
    public void stopConsumers() {
        log.info("Stopping consumers");

        userCreatedConsumer.stop();
//        transactionInProgressConsumer.stop();
        transactionCompletedConsumer.stop();
    }

    @Override
    public Void handleUserCreatedEvent(RBEvent event) {
        log.info("Received user created event: " + event.getId());

        UserCreatedEvent userCreatedEvent = (UserCreatedEvent)event;
        RBResultStatus<Void>result = eventHandlerService.handleUserCreatedEvent(userCreatedEvent);

        if (!result.isSuccess())
            log.error("Failed to handle User Created Event: " + result.getErrorMessage());

        return null;
    }

    @Override
    public Void handleTransactionInProgressEvent(RBEvent event) {
        log.info("Received transaction created event: " + event.getId());

        TransactionInProgressEvent transactionCreatedEvent = (TransactionInProgressEvent)event;
        RBResultStatus<Void>result = eventHandlerService.handleTransactionInProgressEvent(transactionCreatedEvent);

        if (!result.isSuccess())
            log.error("Failed to handle Transaction Created Event: " + result.getErrorMessage());

        return null;
    }

    @Override
    public Void handleTransactionCompletedEvent(RBEvent event) {
        log.info("Received transaction created event: " + event.getId());

        TransactionCompletedEvent transactionCreatedEvent = (TransactionCompletedEvent)event;
        RBResultStatus<Void>result = eventHandlerService.handleTransactionCompletedEvent(transactionCreatedEvent);

        if (!result.isSuccess())
            log.error("Failed to handle Transaction Created Event: " + result.getErrorMessage());

        return null;
    }
}
