package com.rigandbarter.paymentservice.service;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.TransactionCompletedEvent;
import com.rigandbarter.eventlibrary.events.TransactionInProgressEvent;
import com.rigandbarter.eventlibrary.events.UserCreatedEvent;

public interface IEventHandlerService {

    /**
     * Handle the UserCreatedEvent
     * @param userCreatedEvent the event to handle
     * @return the result of the operation
     */
    RBResultStatus<Void> handleUserCreatedEvent(UserCreatedEvent userCreatedEvent);

    /**
     * Handle the TransactionInProgressEvent
     * @param transactionCreatedEvent the event to handle
     * @return the result of the operation
     */
    RBResultStatus<Void> handleTransactionInProgressEvent(TransactionInProgressEvent transactionCreatedEvent);

    /**
     * Handle the TransactionCompletedEvent
     * @param transactionCreatedEvent the event to handle
     * @return the result of the operation
     */
    RBResultStatus<Void> handleTransactionCompletedEvent(TransactionCompletedEvent transactionCreatedEvent);
}
