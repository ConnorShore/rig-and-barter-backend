package com.rigandbarter.paymentservice.service.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.BillingInfoUpdatedEvent;
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
    public RBResultStatus handleUserCreatedEvent(UserCreatedEvent userCreatedEvent) {
        try {
            paymentService.createStripeCustomer(userCreatedEvent.getUserInfo());
        } catch (Exception e) {
            return new RBResultStatus(false, e.getMessage());
        }
        return new RBResultStatus(true);
    }

    @Override
    public RBResultStatus handleBillingInfoUpdatedEvent(BillingInfoUpdatedEvent billingInfoUpdatedEvent) {
        try {
            paymentService.updatedStripeCustomerPaymentInfo(billingInfoUpdatedEvent.getUserId(), billingInfoUpdatedEvent.getBillingInfo());
        } catch (Exception e) {
            return new RBResultStatus(false, e.getMessage());
        }
        return new RBResultStatus(true);
    }

    @Override
    public RBResultStatus handleTransactionInProgressEvent(TransactionInProgressEvent transactionCreatedEvent) {
        // TODO: Create a setupIntent for the buyer to seller (95% of sale price)
        // TODO: Create s setupIntent for buyer to me (5% of sale price)

        return null;
    }

    @Override
    public RBResultStatus handleTransactionCompletedEvent(TransactionCompletedEvent transactionCreatedEvent) {
        // TODO: Complete the setupIntent for the buyer to seller (95% of sale price)
        // TODO: Complete the setupIntent for buyer to me (5% of sale price)

        return null;
    }
}
