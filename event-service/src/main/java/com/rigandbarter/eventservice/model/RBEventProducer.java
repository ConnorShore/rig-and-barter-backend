package com.rigandbarter.eventservice.model;

public abstract class RBEventProducer {

    protected Class<? extends RBEvent> eventType;

    public RBEventProducer(Class<? extends RBEvent> eventType) {
        this.eventType = eventType;
    }
    public abstract void send(RBEvent event);
}
