package com.rigandbarter.notificationservice.consumer;

import com.rigandbarter.eventlibrary.model.RBEvent;

public interface IEventHandler {

    /**
     * Handles the consumption of the Transaction Created Event
     * @param event The Transaction Created Event to handle
     *              TODO: Make this handleEvent() and it will call a switch statement on the impl
     */
    Void handleTransactionCreatedEvent(RBEvent event);

    // TODO: Handle transaction in progress and completed events

    /**
     * Handles the consumption of the User Deleted Event
     * @param event The User Deleted Event to handle
     */
    Void handleUserDeletedEvent(RBEvent event);
}
