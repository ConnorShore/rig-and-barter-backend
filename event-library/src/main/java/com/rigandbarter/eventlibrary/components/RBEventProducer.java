package com.rigandbarter.eventlibrary.components;

import com.rigandbarter.eventlibrary.model.RBEvent;

import java.util.function.Function;

public abstract class RBEventProducer {

    protected Class<? extends RBEvent> eventType;

    public RBEventProducer(Class<? extends RBEvent> eventType) {
        this.eventType = eventType;
    }
    public abstract void send(RBEvent event);
    public abstract void send(RBEvent event, Function<String, Void> onErrorCallback);
}
