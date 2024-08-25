package com.rigandbarter.userservice.consumer.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.core.models.StripeCustomerResponse;
import com.rigandbarter.eventlibrary.components.RBEventConsumer;
import com.rigandbarter.eventlibrary.components.RBEventConsumerFactory;
import com.rigandbarter.eventlibrary.events.StripeCustomerCreatedEvent;
import com.rigandbarter.eventlibrary.events.UserCreatedEvent;
import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.eventlibrary.model.RBEventHandler;
import com.rigandbarter.userservice.consumer.IEventHandler;
import com.rigandbarter.userservice.service.IEventHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IEventHandlerImpl extends RBEventHandler implements IEventHandler {

    private final IEventHandlerService eventHandlerService;

    private RBEventConsumer stripeUserCreatedConsumer;

    public IEventHandlerImpl(RBEventConsumerFactory rbEventConsumerFactory, IEventHandlerService eventHandlerService) {
        super(rbEventConsumerFactory);
        this.eventHandlerService = eventHandlerService;
    }


    @Override
    public void initializeConsumers() {
        log.info("Initializing consumers");

        stripeUserCreatedConsumer = rbEventConsumerFactory.createConsumer(StripeCustomerCreatedEvent.class, this::handleStripeCustomerCreatedEvent);
    }

    @Override
    public void startConsumers() {
        log.info("Starting consumers");

        stripeUserCreatedConsumer.start();
    }

    @Override
    public void stopConsumers() {
        log.info("Stopping consumers");

        stripeUserCreatedConsumer.stop();
    }

    @Override
    public Void handleStripeCustomerCreatedEvent(RBEvent event) {
        log.info("Received stripe customer created event: " + event.getId());

        StripeCustomerCreatedEvent stripeCustomerCreatedEvent = (StripeCustomerCreatedEvent)event;
        RBResultStatus<Void> result = eventHandlerService.handleStripeCustomerCreatedEvent(stripeCustomerCreatedEvent);

        if (!result.isSuccess())
            log.error("Failed to handle Stripe Customer Created Event: " + result.getErrorMessage());

        return null;
    }
}
