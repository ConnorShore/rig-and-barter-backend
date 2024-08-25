package com.rigandbarter.userservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    /**
     * Used for calls between services
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    /**
     * Used for calls to stripe
     */
    @Bean
    public WebClient.Builder webClientBuilderNoLb() {
        return WebClient.builder();
    }
}