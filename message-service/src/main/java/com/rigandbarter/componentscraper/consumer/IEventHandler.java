package com.rigandbarter.componentscraper.consumer;

import com.rigandbarter.eventlibrary.model.RBEvent;

public interface IEventHandler {

    /**
     * Handles the consumption of the Transaction Created Event
     * @param event The Transaction Created Event to handle
     */
    Void handleTransactionCreatedEvent(RBEvent event);
}
