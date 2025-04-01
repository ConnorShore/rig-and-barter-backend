package com.rigandbarter.pcbuilderservice.consumer.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.components.RBEventConsumer;
import com.rigandbarter.eventlibrary.components.RBEventConsumerFactory;
import com.rigandbarter.eventlibrary.events.UserDeletedEvent;
import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.eventlibrary.model.RBEventHandler;
import com.rigandbarter.pcbuilderservice.consumer.IEventHandler;
import com.rigandbarter.pcbuilderservice.service.IEventHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventHandlerImpl extends RBEventHandler implements IEventHandler {

    private final IEventHandlerService eventHandlerService;

    private RBEventConsumer userDeletedConsumer;

    public EventHandlerImpl(RBEventConsumerFactory rbEventConsumerFactory, IEventHandlerService eventHandlerService) {
        super(rbEventConsumerFactory);
        this.eventHandlerService = eventHandlerService;
    }

    @Override
    public void initializeConsumers() {
        userDeletedConsumer = rbEventConsumerFactory.createConsumer(getGroupId(), UserDeletedEvent.class, this::handleUserDeletedEvent);
    }

    @Override
    public void startConsumers() {
        userDeletedConsumer.start();
    }

    @Override
    public void stopConsumers() {
        userDeletedConsumer.stop();
    }

    @Override
    public String getGroupId() {
        return "PCBuilderServiceGroup";
    }

    @Override
    public Void handleUserDeletedEvent(RBEvent event) {
        UserDeletedEvent userDeletedEvent = (UserDeletedEvent) event;
        RBResultStatus<Void> result = eventHandlerService.handleUserDeletedEvent(userDeletedEvent);
        if (!result.isSuccess()) {
            log.error("Failed to handle user deleted event: {}", result.getErrorMessage());
        }
        return null;
    }
}
