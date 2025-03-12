package com.rigandbarter.pcbuilderservice.consumer;

import com.rigandbarter.eventlibrary.model.RBEvent;

public interface IEventHandler {

    /**
     * Handles the consumption of the user verify event
     * @param event The User Deleted Event to handle
     */
    Void handleUserDeletedEvent(RBEvent event);
}
