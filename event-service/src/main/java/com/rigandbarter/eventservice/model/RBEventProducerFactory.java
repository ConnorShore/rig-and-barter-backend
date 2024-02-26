package com.rigandbarter.eventservice.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RBEventProducerFactory {

    @Value("${rb.event.broker}")
    private static String broker;

    public static RBEventProducer createProducer(Class<? extends RBEvent> eventType) {
        String val = broker == null ? "null" : broker;
        switch (val) {
            case "kafka":
            default:
                return new KafkaEventProducerImpl(eventType);
        }
    }
}
