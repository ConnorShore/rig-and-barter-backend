package com.rigandbarter.paymentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @Value("${rb.stripe.fee.percent")
    private String stripeFeePercent;

    @Bean
    public String stripeSecretKey() {
        return secretKey;
    }
    @Bean
    public String stripeFeePercent() {
        return stripeFeePercent;
    }
}