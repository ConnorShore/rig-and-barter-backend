package com.rigandbarter.listingservice.service.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.TransactionCompletedEvent;
import com.rigandbarter.eventlibrary.events.UserVerifyEvent;
import com.rigandbarter.listingservice.service.IEventHandlerService;
import com.rigandbarter.listingservice.service.IListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventHandlerServiceImpl implements IEventHandlerService {

    private final IListingService listingService;

    @Override
    public RBResultStatus<Void> handleUserVerifyEvent(UserVerifyEvent userVerifyEvent) {
        try {
            listingService.setVerificationForListings(userVerifyEvent.getUserId(), userVerifyEvent.isVerified());
        } catch (Exception e) {
            return new RBResultStatus<>(false, e.getMessage());
        }
        return new RBResultStatus<>(true);
    }

    @Override
    public RBResultStatus<Void> handleTransactionCompletedEvent(TransactionCompletedEvent transactionCompletedEvent) {
        try {
            listingService.deleteListingById(transactionCompletedEvent.getListingId(), false, transactionCompletedEvent.getAuthToken());
        } catch (Exception e) {
            return new RBResultStatus<>(false, e.getMessage());
        }
        return new RBResultStatus<>(true);
    }
}
