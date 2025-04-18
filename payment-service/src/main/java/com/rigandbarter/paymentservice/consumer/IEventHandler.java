package com.rigandbarter.paymentservice.consumer;

import com.rigandbarter.eventlibrary.model.RBEvent;

public interface IEventHandler {

    /**
     * Handles the consumption of the user created event
     * @param event The user created event
     */
    Void handleUserCreatedEvent(RBEvent event);

    /**
     * Handles the consumption of the Transaction In Progress Event
     * @param event The Transaction In Progress Event to handle
     */
    Void handleTransactionInProgressEvent(RBEvent event);

    /**
     * Handles the consumption of the Transaction Completed Event
     * @param event The Transaction Completed Event to handle
     */
    Void handleTransactionCompletedEvent(RBEvent event);

    /**
     * Handles the consumption of the User Deleted Event
     * @param event The User Deleted Event to handle
     */
    Void handleUserDeletedEvent(RBEvent event);
}
