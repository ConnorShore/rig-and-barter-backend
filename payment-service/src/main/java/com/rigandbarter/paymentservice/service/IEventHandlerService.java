package com.rigandbarter.paymentservice.service;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.BillingInfoUpdatedEvent;
import com.rigandbarter.eventlibrary.events.TransactionCompletedEvent;
import com.rigandbarter.eventlibrary.events.TransactionInProgressEvent;
import com.rigandbarter.eventlibrary.events.UserCreatedEvent;

public interface IEventHandlerService {
    RBResultStatus<Void>handleUserCreatedEvent(UserCreatedEvent userCreatedEvent);

    RBResultStatus<Void>handleBillingInfoUpdatedEvent(BillingInfoUpdatedEvent billingInfoUpdatedEvent);

    RBResultStatus<Void>handleTransactionInProgressEvent(TransactionInProgressEvent transactionCreatedEvent);

    RBResultStatus<Void>handleTransactionCompletedEvent(TransactionCompletedEvent transactionCreatedEvent);
}
