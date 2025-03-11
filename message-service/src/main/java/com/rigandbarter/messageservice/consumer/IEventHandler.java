package com.rigandbarter.messageservice.consumer;

import com.rigandbarter.eventlibrary.model.RBEvent;

public interface IEventHandler {

    /**
     * Handles the consumption of the Transaction Created Event
     * @param event The Transaction Created Event to handle
     */
    Void handleTransactionCreatedEvent(RBEvent event);

    /**
     * Handles the consumption of the User Deleted Event
     * @param event The User Deleted Event to handle
     */
    Void handleUserDeletedEvent(RBEvent event);
}
