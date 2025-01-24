package com.rigandbarter.messageservice.consumer.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.components.RBEventConsumer;
import com.rigandbarter.eventlibrary.components.RBEventConsumerFactory;
import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.eventlibrary.model.RBEventHandler;
import com.rigandbarter.messageservice.consumer.IEventHandler;
import com.rigandbarter.messageservice.service.IEventHandlerService;
import com.rigandbarter.messageservice.service.impl.EventHandlerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventHandlerImpl extends RBEventHandler implements IEventHandler {

    private final IEventHandlerService eventHandlerService;

    private RBEventConsumer transactionCreatedConsumer;

    public EventHandlerImpl(RBEventConsumerFactory rbEventConsumerFactory, EventHandlerServiceImpl eventHandlerService) {
        super(rbEventConsumerFactory);
        this.eventHandlerService = eventHandlerService;
    }

    @Override
    public void initializeConsumers() {
        log.info("Initializing consumers");
        transactionCreatedConsumer = rbEventConsumerFactory.createConsumer(getGroupId(), TransactionCreatedEvent.class, this::handleTransactionCreatedEvent);
    }

    @Override
    public void startConsumers() {
        log.info("Starting consumers");
        transactionCreatedConsumer.start();
    }

    @Override
    public void stopConsumers() {
        log.info("Stopping consumers");
        transactionCreatedConsumer.stop();
    }

    @Override
    public String getGroupId() {
        return "MessageServiceGroup";
    }

    @Override
    public Void handleTransactionCreatedEvent(RBEvent event) {
        log.info("Received transaction created event: " + event.getId());

        TransactionCreatedEvent transactionCreatedEvent = (TransactionCreatedEvent)event;
        RBResultStatus<Void> result = eventHandlerService.handleTransactionCreatedEvent(transactionCreatedEvent);

        if (!result.isSuccess())
            log.error("Failed to handle Transaction Created Event: " + result.getErrorMessage());

        return null;
    }
}
