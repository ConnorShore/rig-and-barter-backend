package com.rigandbarter.transactionservice.service.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.UserDeletedEvent;
import com.rigandbarter.transactionservice.service.IEventHandlerService;
import com.rigandbarter.transactionservice.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventHandlerServiceImpl implements IEventHandlerService {

    private final ITransactionService transactionService;

    @Override
    public RBResultStatus<Void> handleUserDeletedEvent(UserDeletedEvent userDeletedEvent) {
        try {
            transactionService.deleteTransactionsForUser(userDeletedEvent.getUserId());
        } catch (Exception e) {
            return new RBResultStatus<>(false, e.getMessage());
        }
        return new RBResultStatus<>(true);
    }
}
