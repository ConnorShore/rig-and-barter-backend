package com.rigandbarter.userservice.service;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.StripeCustomerCreatedEvent;

public interface IEventHandlerService {
    RBResultStatus<Void> handleStripeCustomerCreatedEvent(StripeCustomerCreatedEvent stripeCustomerCreatedEvent);
}
