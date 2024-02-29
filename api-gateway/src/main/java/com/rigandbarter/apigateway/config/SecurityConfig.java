package com.rigandbarter.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${rb.front-end.url}")
    private String FRONT_END_URL;

    @Bean
    public CorsWebFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials( true );
        config.setAllowedOriginPatterns( List.of( "*" ) );
        config.setAllowedMethods( List.of( "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD" ) );
        config.setAllowedHeaders( List.of( "origin", "content-type", "accept", "authorization", "cookie" ) );
//
        CorsConfiguration config2 = new CorsConfiguration();
        config2.setAllowCredentials( false );
        config2.setAllowedOriginPatterns( List.of( "*" ) );
        config2.setAllowedMethods( List.of( "*" ) );
        config2.setAllowedHeaders( List.of( "*" ) );

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration( "/api/**", config );
        source.registerCorsConfiguration( "/**", config );
        source.registerCorsConfiguration( "/socket/**", config2 );

        return new CorsWebFilter( source );
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/**").permitAll()
                        .anyExchange().authenticated())
                .oauth2Login(Customizer.withDefaults())
                .build();
    }

}