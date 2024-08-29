package com.rigandbarter.messageservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class MessageWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${rb.front-end.url}")
    private String FRONT_END_URL;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/mapp");
        registry.enableSimpleBroker("/mtopic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/msocket")
                .setAllowedOriginPatterns(FRONT_END_URL)
                .withSockJS();
    }
}
