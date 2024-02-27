package com.rigandbarter.eventlibrary.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.eventlibrary.components.RBEventConsumerFactory;
import com.rigandbarter.eventlibrary.components.RBEventProducerFactory;
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
