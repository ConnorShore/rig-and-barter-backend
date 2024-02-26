package com.rigandbarter.eventservice.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class RBEventConsumerFactory {

    @Value("${rb.event.broker}")
    private static String broker;

    public static RBEventConsumer createConsumer(Class<? extends RBEvent> eventType, Function<RBEvent, Void> handle) {
        String val = broker == null ? "null" : broker;
        switch (val) {
            case "kafka":
            default:
                return new KakfaEventConsumerImpl(eventType.getSimpleName(), handle, eventType);
        }
    }
}
