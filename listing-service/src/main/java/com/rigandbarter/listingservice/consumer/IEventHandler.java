package com.rigandbarter.listingservice.consumer;

import com.rigandbarter.eventlibrary.model.RBEvent;

public interface IEventHandler {

    /**
     * Handles the consumption of the user verify event
     * @param event
     * @return
     */
    Void handleUserVerifyEvent(RBEvent event);
}
