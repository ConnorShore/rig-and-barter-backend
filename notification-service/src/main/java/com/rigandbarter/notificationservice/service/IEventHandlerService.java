package com.rigandbarter.notificationservice.service;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;

public interface IEventHandlerService {

    /**
     * Handle the transaction created event
     * @param event The transaciton created event to handle
     * @return The result of handling the event
     */
    RBResultStatus<Void>handleTransactionCreatedEvent(TransactionCreatedEvent event);
}
