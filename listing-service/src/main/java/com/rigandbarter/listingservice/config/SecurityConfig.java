package com.rigandbarter.listingservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

//    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
//    String issuerUri;
//
//    @Value("${rb.security.permitted-get-urls}")
//    String[] permittedGetUrls;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(authorize -> authorize
                .anyRequest()
                .permitAll())
                .build();
//        return httpSecurity.authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(freeResourceUrls)
//                        .permitAll()
//                        .anyRequest().authenticated())
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
//                .build();
    }

}