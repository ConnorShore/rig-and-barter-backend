package com.rigandbarter.notificationservice.service;

import com.rigandbarter.core.models.RBReturnStatus;
import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;

public interface IEventHandlerService {

    /**
     * Handle the transaction created event
     * @param event The transaciton created event to handle
     * @return The result of handling the event
     */
    RBReturnStatus handleTransactionCreatedEvent(TransactionCreatedEvent event);
}
