package com.rigandbarter.eventservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.eventservice.components.RBEventConsumerFactory;
import com.rigandbarter.eventservice.components.RBEventProducerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RBEventConfiguration {

    private final ObjectMapper objectMapper;

    public RBEventConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.findAndRegisterModules();
    }

    @Bean
    RBEventProducerFactory rbEventProducerFactory() {
        return new RBEventProducerFactory(objectMapper);
    }
    @Bean
    RBEventConsumerFactory rbEventConsumerFactory() {
        return new RBEventConsumerFactory(objectMapper);
    }
}
