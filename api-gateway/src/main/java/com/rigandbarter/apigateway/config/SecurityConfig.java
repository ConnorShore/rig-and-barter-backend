package com.rigandbarter.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Value("${rb.front-end.url}")
    private String FRONT_END_URL;

    String[] permittedGetUrls = {
            "/api/listing/**",
            "/api/component/**",
            "/api/message/status",
            "/api/notification/status",
            "/api/payment/status",
            "/api/payment/reauth",
            "/api/account/*/delete",
            "/api/transaction/status",
            "/api/pc-builder/status",
            "/api/user/status"
    };

    String[] permittedPostUrls = {
            "/api/component",
            "/api/user",
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // TODO: Move permitted urls to objects in code
        //  see if can remove security config from other services
        //  get front end flow working (create user works, but front end needs to use new keycloak stuff)
        return http
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.ignoringRequestMatchers(permittedPostUrls))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, permittedPostUrls).permitAll()
                        .requestMatchers(HttpMethod.GET, permittedGetUrls).permitAll()
                        .anyRequest().authenticated())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}