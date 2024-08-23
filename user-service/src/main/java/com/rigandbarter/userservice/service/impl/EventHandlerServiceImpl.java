package com.rigandbarter.userservice.service.impl;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.StripeCustomerCreatedEvent;
import com.rigandbarter.userservice.service.IEventHandlerService;
import com.rigandbarter.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventHandlerServiceImpl implements IEventHandlerService {

    private final IUserService userService;

    @Override
    public RBResultStatus<Void> handleStripeCustomerCreatedEvent(StripeCustomerCreatedEvent stripeCustomerCreatedEvent) {
        try {
            userService.setUserStripeCustomerInfo(stripeCustomerCreatedEvent);
        } catch (Exception e) {
            return new RBResultStatus<>(false, e.getMessage());
        }

        return new RBResultStatus<>(true);
    }
}
