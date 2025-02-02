package com.rigandbarter.listingservice.service;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.TransactionCompletedEvent;
import com.rigandbarter.eventlibrary.events.UserVerifyEvent;

public interface IEventHandlerService {

    /**
     * Handles the user verify event
     * @param userVerifyEvent The event to handle
     * @return Success status
     */
    RBResultStatus<Void> handleUserVerifyEvent(UserVerifyEvent userVerifyEvent);

    /**
     * Handles the transaction completed event
     * @param transactionCompletedEvent The event to handle
     * @return Success status
     */
    RBResultStatus<Void> handleTransactionCompletedEvent(TransactionCompletedEvent transactionCompletedEvent);
}
