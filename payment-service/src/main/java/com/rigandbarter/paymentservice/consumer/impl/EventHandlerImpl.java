package com.rigandbarter.paymentservice.consumer.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.components.RBEventConsumer;
import com.rigandbarter.eventlibrary.components.RBEventConsumerFactory;
import com.rigandbarter.eventlibrary.events.TransactionCompletedEvent;
import com.rigandbarter.eventlibrary.events.TransactionInProgressEvent;
import com.rigandbarter.eventlibrary.events.UserCreatedEvent;
import com.rigandbarter.eventlibrary.events.UserDeletedEvent;
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
    // TODO: Bring in progress consumner back online??
//    private RBEventConsumer transactionInProgressConsumer;
    private RBEventConsumer transactionCompletedConsumer;
    private RBEventConsumer userDeletedConsumer;

    public EventHandlerImpl(RBEventConsumerFactory rbEventConsumerFactory, IEventHandlerService eventHandlerService) {
        super(rbEventConsumerFactory);
        this.eventHandlerService = eventHandlerService;
    }

    @Override
    public void initializeConsumers() {
        log.info("Initializing consumers");

        userCreatedConsumer = rbEventConsumerFactory.createConsumer(getGroupId(), UserCreatedEvent.class, this::handleUserCreatedEvent);
//        transactionInProgressConsumer = rbEventConsumerFactory.createConsumer(TransactionInProgressEvent.class, this::handleTransactionInProgressEvent);
        transactionCompletedConsumer = rbEventConsumerFactory.createConsumer(getGroupId(), TransactionCompletedEvent.class, this::handleTransactionCompletedEvent);
        userDeletedConsumer = rbEventConsumerFactory.createConsumer(getGroupId(), UserDeletedEvent.class, this::handleUserDeletedEvent);
    }

    @Override
    public void startConsumers() {
        log.info("Starting consumers");

        userCreatedConsumer.start();
//        transactionInProgressConsumer.start();
        transactionCompletedConsumer.start();
        userDeletedConsumer.start();
    }

    @Override
    public void stopConsumers() {
        log.info("Stopping consumers");

        userCreatedConsumer.stop();
//        transactionInProgressConsumer.stop();
        transactionCompletedConsumer.stop();
        userDeletedConsumer.stop();
    }

    @Override
    public String getGroupId() {
        return "PaymentServiceGroup";
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
        log.info("Received transaction in progress event: " + event.getId());

        TransactionInProgressEvent transactionInProgressEvent = (TransactionInProgressEvent)event;
        RBResultStatus<Void>result = eventHandlerService.handleTransactionInProgressEvent(transactionInProgressEvent);

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
            log.error("Failed to handle Transaction Completed Event: " + result.getErrorMessage());

        return null;
    }

    @Override
    public Void handleUserDeletedEvent(RBEvent event) {
        log.info("Received user deleted event: " + event.getId());

        UserDeletedEvent userDeletedEvent = (UserDeletedEvent) event;
        RBResultStatus<Void> result = eventHandlerService.handleUserDeletedEvent(userDeletedEvent);

        if (!result.isSuccess())
            log.error("Failed to handle User Deleted Event: " + result.getErrorMessage());

        return null;
    }
}
