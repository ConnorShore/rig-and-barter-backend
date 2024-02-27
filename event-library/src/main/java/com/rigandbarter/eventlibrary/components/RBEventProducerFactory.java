package com.rigandbarter.eventlibrary.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.eventlibrary.components.kafka.KafkaEventProducerImpl;
import com.rigandbarter.eventlibrary.config.RBEventProperties;
import com.rigandbarter.eventlibrary.model.RBEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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
