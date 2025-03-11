package com.rigandbarter.notificationservice.service;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.eventlibrary.events.UserDeletedEvent;

public interface IEventHandlerService {

    /**
     * Handle the transaction created event
     * @param event The transaction created event to handle
     * @return The result of handling the event
     */
    RBResultStatus<Void>handleTransactionCreatedEvent(TransactionCreatedEvent event);

    /**
     * Handle the user deleted event
     * @param userDeletedEvent The user deleted event to handle
     * @return The result of handling the event
     */
    RBResultStatus<Void> handleUserDeletedEvent(UserDeletedEvent userDeletedEvent);
}
