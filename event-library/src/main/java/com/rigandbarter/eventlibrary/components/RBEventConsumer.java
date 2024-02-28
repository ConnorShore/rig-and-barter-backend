package com.rigandbarter.eventlibrary.components;

import com.rigandbarter.eventlibrary.model.RBEvent;

import java.util.function.Function;

public abstract class RBEventConsumer {

    protected Function<RBEvent, Void> handlerFunction;
    protected Class<? extends RBEvent> eventType;

    public RBEventConsumer(Function<RBEvent, Void> handlerFunction, Class<? extends RBEvent> eventType) {
        this.handlerFunction = handlerFunction;
        this.eventType = eventType;
    }

    protected abstract void handleEvent(RBEvent event);
    public abstract void start();
    public abstract void stop();
}
