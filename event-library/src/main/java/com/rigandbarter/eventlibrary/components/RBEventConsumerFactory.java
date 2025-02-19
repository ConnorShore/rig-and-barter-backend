package com.rigandbarter.eventlibrary.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.eventlibrary.components.kafka.KakfaEventConsumerImpl;
import com.rigandbarter.eventlibrary.config.RBEventProperties;
import com.rigandbarter.eventlibrary.model.RBEvent;
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

    public RBEventConsumer createConsumer(String groupId, Class<? extends RBEvent> eventType, Function<RBEvent, Void> handle) {
        String brokerEnv = environment.getProperty(RBEventProperties.RB_EVENT_BROKER);

        if(brokerEnv == null)
            throw new RuntimeException("RB Event Broker Not Set");

        switch (brokerEnv) {
            case "kafka":
            default:
                return new KakfaEventConsumerImpl(groupId, eventType.getSimpleName(), handle, eventType, environment, objectMapper);
        }
    }
}
