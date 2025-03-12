package com.rigandbarter.pcbuilderservice.service;

import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.UserDeletedEvent;

public interface IEventHandlerService {

    /**
     * Handles the user deleted event
     * @param userDeletedEvent The event to handle
     * @return Success status
     */
    RBResultStatus<Void> handleUserDeletedEvent(UserDeletedEvent userDeletedEvent);
}
