package com.rigandbarter.listingservice.consumer;

import com.rigandbarter.eventlibrary.model.RBEvent;

public interface IEventHandler {

    /**
     * Handles the consumption of the user verify event
     * @param event the event to handle
     */
    Void handleUserVerifyEvent(RBEvent event);

    /**
     * Handles the consumption of the transaction completed event
     * @param event the event to handle
     */
    Void handleTransactionCompletedEvent(RBEvent event);

    /**
     * Handles the consumption of the user deleted event
     * @param event the event to handle
     */
    Void handleUserDeletedEvent(RBEvent event);
}
