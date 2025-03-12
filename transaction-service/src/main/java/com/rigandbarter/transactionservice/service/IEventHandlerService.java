package com.rigandbarter.transactionservice.service;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.UserDeletedEvent;

public interface IEventHandlerService {

    /**
     * Handles the user deleted event
     * @param userDeletedEvent The event to handle
     * @return The result of handling the event
     */
    RBResultStatus<Void> handleUserDeletedEvent(UserDeletedEvent userDeletedEvent);
}
