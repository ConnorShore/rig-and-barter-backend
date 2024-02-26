package com.rigandbarter.eventservice.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RBEventProducerFactory {


    @Value("${rb.event.broker}")
    private static String broker;

    private static ObjectMapper objectMapper;

    public RBEventProducerFactory() {
    }

    public static RBEventProducer createProducer(Class<? extends RBEvent> eventType) {
        if(objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
        }

        String val = broker == null ? "null" : broker;
        switch (val) {
            case "kafka":
            default:
                return new KafkaEventProducerImpl(eventType, objectMapper);
        }
    }
}
