package com.rigandbarter.paymentservice.service;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.BillingInfoUpdatedEvent;
import com.rigandbarter.eventlibrary.events.TransactionCompletedEvent;
import com.rigandbarter.eventlibrary.events.TransactionInProgressEvent;
import com.rigandbarter.eventlibrary.events.UserCreatedEvent;

public interface IEventHandlerService {
    RBResultStatus handleUserCreatedEvent(UserCreatedEvent userCreatedEvent);

    RBResultStatus handleBillingInfoUpdatedEvent(BillingInfoUpdatedEvent billingInfoUpdatedEvent);

    RBResultStatus handleTransactionInProgressEvent(TransactionInProgressEvent transactionCreatedEvent);

    RBResultStatus handleTransactionCompletedEvent(TransactionCompletedEvent transactionCreatedEvent);
}
