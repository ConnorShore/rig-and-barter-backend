package com.rigandbarter.notificationservice.service;

import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.eventlibrary.model.RBEventResult;

public interface IEventHandlerService {

    /**
     * Handle the transaction created event
     * @param event The transaciton created event to handle
     * @return The result of handling the event
     */
    RBEventResult handleTransactionCreatedEvent(TransactionCreatedEvent event);
}
