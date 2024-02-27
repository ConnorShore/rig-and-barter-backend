package com.rigandbarter.notificationservice.consumer;

import com.rigandbarter.eventlibrary.components.RBEventConsumer;
import com.rigandbarter.eventlibrary.components.RBEventConsumerFactory;
import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.eventlibrary.model.RBEventHandler;
import org.springframework.stereotype.Service;

@Service
public class EventHandler extends RBEventHandler {

    private RBEventConsumer transactionCreatedConsumer;

    public EventHandler(RBEventConsumerFactory rbEventConsumerFactory) {
        super(rbEventConsumerFactory);
    }

    @Override
    public void initializeConsumers() {
        transactionCreatedConsumer = rbEventConsumerFactory.createConsumer(TransactionCreatedEvent.class, this::handleTransactionCreatedEvent);
    }

    @Override
    public void startConsumers() {
        transactionCreatedConsumer.start();
    }

    @Override
    public void stopConsumers() {
        transactionCreatedConsumer.stop();
    }

    private Void handleTransactionCreatedEvent(RBEvent evt) {
        TransactionCreatedEvent event = (TransactionCreatedEvent)evt;
        System.out.println(event);
        return null;
    }
}
