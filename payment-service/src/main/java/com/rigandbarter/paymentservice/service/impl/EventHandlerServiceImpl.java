package com.rigandbarter.paymentservice.service.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.TransactionCompletedEvent;
import com.rigandbarter.eventlibrary.events.TransactionInProgressEvent;
import com.rigandbarter.eventlibrary.events.UserCreatedEvent;
import com.rigandbarter.paymentservice.service.IEventHandlerService;
import com.rigandbarter.paymentservice.service.IPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventHandlerServiceImpl implements IEventHandlerService {

    private final IPaymentService paymentService;

    @Override
    public RBResultStatus<Void> handleUserCreatedEvent(UserCreatedEvent userCreatedEvent) {
        try {
            paymentService.createStripeCustomer(userCreatedEvent.getUserInfo());
        } catch (Exception e) {
            return new RBResultStatus<>(false, e.getMessage());
        }
        return new RBResultStatus<>(true);
    }

    @Override
    public RBResultStatus<Void> handleTransactionInProgressEvent(TransactionInProgressEvent transactionCreatedEvent) {
        // Currently don't need to do anything for this...may remove in future. Leaving just in case its needed
        return new RBResultStatus<>(true);
    }

    @Override
    public RBResultStatus<Void> handleTransactionCompletedEvent(TransactionCompletedEvent transactionCreatedEvent) {
        try {
            paymentService.completeTransaction(transactionCreatedEvent);
        } catch (Exception e) {
            return new RBResultStatus<>(false, e.getMessage());
        }

        return new RBResultStatus<>(true);
    }
}
