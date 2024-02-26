package com.rigandbarter.eventservice.model;

import java.util.function.Function;

public abstract class RBEventConsumer {

    protected Function<RBEvent, Void> handlerFunction;
    protected Class<? extends RBEvent> eventType;

    public RBEventConsumer(Function<RBEvent, Void> handlerFunction, Class<? extends RBEvent> eventType) {
        this.handlerFunction = handlerFunction;
        this.eventType = eventType;
    }

    public abstract void handleEvent(RBEvent event);
}
