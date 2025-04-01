package com.rigandbarter.listingservice.consumer.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.components.RBEventConsumer;
import com.rigandbarter.eventlibrary.components.RBEventConsumerFactory;
import com.rigandbarter.eventlibrary.events.TransactionCompletedEvent;
import com.rigandbarter.eventlibrary.events.UserCreatedEvent;
import com.rigandbarter.eventlibrary.events.UserDeletedEvent;
import com.rigandbarter.eventlibrary.events.UserVerifyEvent;
import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.eventlibrary.model.RBEventHandler;
import com.rigandbarter.listingservice.consumer.IEventHandler;
import com.rigandbarter.listingservice.service.IEventHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventHandlerImpl extends RBEventHandler implements IEventHandler {

    private final IEventHandlerService eventHandlerService;

    private RBEventConsumer verifyUserConsumer;
    private RBEventConsumer transactionCompletedConsumer;
    private RBEventConsumer deleteUserConsumer;

    public EventHandlerImpl(RBEventConsumerFactory rbEventConsumerFactory, IEventHandlerService eventHandlerService) {
        super(rbEventConsumerFactory);
        this.eventHandlerService = eventHandlerService;
    }

    @Override
    public void initializeConsumers() {
        log.info("Initializing consumers");

        verifyUserConsumer = rbEventConsumerFactory.createConsumer(getGroupId(), UserVerifyEvent.class, this::handleUserVerifyEvent);
        transactionCompletedConsumer = rbEventConsumerFactory.createConsumer(getGroupId(), TransactionCompletedEvent.class, this::handleTransactionCompletedEvent);
        deleteUserConsumer = rbEventConsumerFactory.createConsumer(getGroupId(), UserDeletedEvent.class, this::handleUserDeletedEvent);
    }

    @Override
    public void startConsumers() {
        log.info("Starting consumers");

        verifyUserConsumer.start();
        transactionCompletedConsumer.start();
        deleteUserConsumer.start();
    }

    @Override
    public void stopConsumers() {
        log.info("Stopping consumers");

        verifyUserConsumer.stop();
        transactionCompletedConsumer.stop();
        deleteUserConsumer.stop();
    }

    @Override
    public String getGroupId() {
        return "ListingServiceGroup";
    }

    @Override
    public Void handleUserVerifyEvent(RBEvent event) {
        log.info("Received user created event: " + event.getId());

        UserVerifyEvent userVerifyEvent = (UserVerifyEvent)event;
        RBResultStatus<Void> result = eventHandlerService.handleUserVerifyEvent(userVerifyEvent);

        if (!result.isSuccess())
            log.error("Failed to handle User Verify Event: " + result.getErrorMessage());

        return null;
    }

    @Override
    public Void handleTransactionCompletedEvent(RBEvent event) {
        log.info("Received transaction compoleted event: " + event.getId());

        TransactionCompletedEvent transactionCompletedEvent = (TransactionCompletedEvent)event;
        RBResultStatus<Void> result = eventHandlerService.handleTransactionCompletedEvent(transactionCompletedEvent);

        if (!result.isSuccess())
            log.error("Failed to handle transaction compoleted Event: " + result.getErrorMessage());

        return null;
    }

    @Override
    public Void handleUserDeletedEvent(RBEvent event) {
        log.info("Recieved user deleted event: " + event.getId());

        UserDeletedEvent userDeletedEvent = (UserDeletedEvent)event;
        RBResultStatus<Void> result = eventHandlerService.handleUserDeleteEvent(userDeletedEvent);

        if (!result.isSuccess())
            log.error("Failed to handle user deleted Event: " + result.getErrorMessage());

        return null;
    }
}
