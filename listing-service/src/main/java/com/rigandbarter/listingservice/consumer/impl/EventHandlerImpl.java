package com.rigandbarter.listingservice.consumer.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.components.RBEventConsumer;
import com.rigandbarter.eventlibrary.components.RBEventConsumerFactory;
import com.rigandbarter.eventlibrary.events.UserCreatedEvent;
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

    public EventHandlerImpl(RBEventConsumerFactory rbEventConsumerFactory, IEventHandlerService eventHandlerService) {
        super(rbEventConsumerFactory);
        this.eventHandlerService = eventHandlerService;
    }

    @Override
    public void initializeConsumers() {
        log.info("Initializing consumers");

        verifyUserConsumer = rbEventConsumerFactory.createConsumer(UserVerifyEvent.class, this::handleUserVerifyEvent);
    }

    @Override
    public void startConsumers() {
        log.info("Starting consumers");

        verifyUserConsumer.start();
    }

    @Override
    public void stopConsumers() {
        log.info("Stopping consumers");

        verifyUserConsumer.stop();
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
}
