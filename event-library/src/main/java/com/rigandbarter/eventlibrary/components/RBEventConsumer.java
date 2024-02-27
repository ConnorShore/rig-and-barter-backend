package com.rigandbarter.eventlibrary.components;

import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.eventlibrary.model.RBEventResult;

import java.util.function.Function;

public abstract class RBEventConsumer {

    protected Function<RBEvent, RBEventResult> handlerFunction;
    protected Class<? extends RBEvent> eventType;

    public RBEventConsumer(Function<RBEvent, RBEventResult> handlerFunction, Class<? extends RBEvent> eventType) {
        this.handlerFunction = handlerFunction;
        this.eventType = eventType;
    }

    public abstract void handleEvent(RBEvent event);
    public abstract void start();
    public abstract void stop();
}
