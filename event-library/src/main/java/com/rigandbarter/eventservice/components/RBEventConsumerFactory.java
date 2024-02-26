package com.rigandbarter.eventservice.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.eventservice.components.kafka.KakfaEventConsumerImpl;
import com.rigandbarter.eventservice.config.RBEventProperties;
import com.rigandbarter.eventservice.model.RBEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class RBEventConsumerFactory {
    @Autowired
    Environment environment;

    private final ObjectMapper objectMapper;

    public RBEventConsumer createConsumer(Class<? extends RBEvent> eventType, Function<RBEvent, Void> handle) {
        String brokerEnv = environment.getProperty(RBEventProperties.RB_EVENT_BROKER);

        if(brokerEnv == null)
            throw new RuntimeException("RB Event Broker Not Set");

        switch (brokerEnv) {
            case "kafka":
            default:
                return new KakfaEventConsumerImpl(eventType.getSimpleName(), handle, eventType, environment, objectMapper);
        }
    }
}
