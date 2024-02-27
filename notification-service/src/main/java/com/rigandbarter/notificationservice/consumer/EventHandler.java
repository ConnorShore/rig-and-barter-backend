package com.rigandbarter.notificationservice.consumer;

import com.rigandbarter.eventlibrary.components.RBEventConsumer;
import com.rigandbarter.eventlibrary.components.RBEventConsumerFactory;
import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.eventlibrary.model.RBEventHandler;
import com.rigandbarter.eventlibrary.model.RBEventResult;
import com.rigandbarter.notificationservice.service.EventHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventHandler extends RBEventHandler {

    private final EventHandlerService eventHandlerService;

    private RBEventConsumer transactionCreatedConsumer;

    public EventHandler(RBEventConsumerFactory rbEventConsumerFactory, EventHandlerService eventHandlerService) {
        super(rbEventConsumerFactory);
        this.eventHandlerService = eventHandlerService;
    }

    @Override
    public void initializeConsumers() {
        log.info("Initializing consumers");
        transactionCreatedConsumer = rbEventConsumerFactory.createConsumer(TransactionCreatedEvent.class, this::handleTransactionCreatedEvent);
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

    private RBEventResult handleTransactionCreatedEvent(RBEvent evt) {
        log.info("Received transaction created event: " + evt.getId());
        TransactionCreatedEvent event = (TransactionCreatedEvent)evt;
        return eventHandlerService.handleTransactionCreatedEvent(event);
    }
}
