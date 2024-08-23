package com.rigandbarter.userservice.consumer;

import com.rigandbarter.eventlibrary.model.RBEvent;

public interface IEventHandler {

    /**
     * Handles the Stripe Customer Created event
     * @param event Stripe Customer Created event ot handle
     */
    Void handleStripeCustomerCreatedEvent(RBEvent event);
}
