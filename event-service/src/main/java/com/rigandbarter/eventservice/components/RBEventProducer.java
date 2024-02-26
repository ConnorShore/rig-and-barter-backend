package com.rigandbarter.eventservice.components;

import com.rigandbarter.eventservice.model.RBEvent;
import org.springframework.stereotype.Component;

public abstract class RBEventProducer {

    protected Class<? extends RBEvent> eventType;

    public RBEventProducer(Class<? extends RBEvent> eventType) {
        this.eventType = eventType;
    }
    public abstract void send(RBEvent event);
}
