package com.rigandbarter.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
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

    String[] permitAllUrls = {
            "/socket/**",
            "/msocket/**",
    };

    String[] csrfIgnoredUrls = Stream.concat(Arrays.stream(permitAllUrls), Arrays.stream(permittedPostUrls)).toArray(String[]::new);

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.ignoringRequestMatchers(csrfIgnoredUrls))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permitAllUrls).permitAll()
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
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedOrigins(List.of(FRONT_END_URL));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }
}