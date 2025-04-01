package com.rigandbarter.notificationservice.consumer.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.components.RBEventConsumer;
import com.rigandbarter.eventlibrary.components.RBEventConsumerFactory;
import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.eventlibrary.events.UserDeletedEvent;
import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.eventlibrary.model.RBEventHandler;
import com.rigandbarter.notificationservice.consumer.IEventHandler;
import com.rigandbarter.notificationservice.service.IEventHandlerService;
import com.rigandbarter.notificationservice.service.impl.EventHandlerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventHandlerImpl extends RBEventHandler implements IEventHandler {

    private final IEventHandlerService eventHandlerService;

    private RBEventConsumer transactionCreatedConsumer;
    private RBEventConsumer userDeletedConsumer;

    public EventHandlerImpl(RBEventConsumerFactory rbEventConsumerFactory, EventHandlerServiceImpl eventHandlerService) {
        super(rbEventConsumerFactory);
        this.eventHandlerService = eventHandlerService;
    }

    @Override
    public void initializeConsumers() {
        log.info("Initializing consumers");
        transactionCreatedConsumer = rbEventConsumerFactory.createConsumer(getGroupId(), TransactionCreatedEvent.class, this::handleTransactionCreatedEvent);
        userDeletedConsumer = rbEventConsumerFactory.createConsumer(getGroupId(), UserDeletedEvent.class, this::handleUserDeletedEvent);
    }

    @Override
    public void startConsumers() {
        log.info("Starting consumers");
        transactionCreatedConsumer.start();
        userDeletedConsumer.start();
    }

    @Override
    public void stopConsumers() {
        log.info("Stopping consumers");
        transactionCreatedConsumer.stop();
        userDeletedConsumer.stop();
    }

    @Override
    public String getGroupId() {
        return "NotificationServiceGroup";
    }

    @Override
    public Void handleTransactionCreatedEvent(RBEvent event) {
        log.info("Received transaction created event: " + event.getId());

        TransactionCreatedEvent transactionCreatedEvent = (TransactionCreatedEvent)event;
        RBResultStatus<Void>result = eventHandlerService.handleTransactionCreatedEvent(transactionCreatedEvent);

        if (!result.isSuccess())
            log.error("Failed to handle Transaction Created Event: " + result.getErrorMessage());

        return null;
    }

    @Override
    public Void handleUserDeletedEvent(RBEvent event) {
        log.info("Received user deleted event: " + event.getId());

        UserDeletedEvent userDeletedEvent = (UserDeletedEvent)event;
        RBResultStatus<Void>result = eventHandlerService.handleUserDeletedEvent(userDeletedEvent);

        if (!result.isSuccess())
            log.error("Failed to handle User Deleted Event: " + result.getErrorMessage());

        return null;
    }

    // TODO: Handle transaction in progress and completed events
}
