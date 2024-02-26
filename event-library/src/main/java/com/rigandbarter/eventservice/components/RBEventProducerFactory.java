package com.rigandbarter.eventservice.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.eventservice.components.kafka.KafkaEventProducerImpl;
import com.rigandbarter.eventservice.config.RBEventProperties;
import com.rigandbarter.eventservice.model.RBEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class RBEventProducerFactory {

    @Autowired
    Environment environment;

    private final ObjectMapper objectMapper;

    public RBEventProducer createProducer(Class<? extends RBEvent> eventType) {
        String brokerEnv = environment.getProperty(RBEventProperties.RB_EVENT_BROKER);

        if(brokerEnv == null)
            throw new RuntimeException("RB Event Broker Not Set");

        switch (brokerEnv) {
            case "kafka":
            default:
                return new KafkaEventProducerImpl(eventType, environment, objectMapper);
        }
    }
}
