package com.rigandbarter.apigateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {


    @Value("${listing.service.url}")
    private String listingServiceUrl;

    @Value("${transaction.service.url}")
    private String transactionServiceUrl;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    // TODO: Figure out websocked route (notification)
//    @Value("${websocket.service.url}")
//    private String webSocketServiceUrl;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    @Value("${message.service.url}")
    private String messageServiceUrl;

//  TODO: Message websocked
//    @Value("${notification.service.url}")
//    private String notificationServiceUrl;

    @Value("${component.service.url}")
    private String componentServiceUrl;

    @Value("${pc-builder.service.url}")
    private String pcBuilderServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> listingServiceRoute() {
        return GatewayRouterFunctions.route("listing_service")
                .route(RequestPredicates.path("/api/listing/**"), HandlerFunctions.http(listingServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> transactionServiceRoute() {
        return GatewayRouterFunctions.route("transaction_service")
                .route(RequestPredicates.path("/api/transaction/**"), HandlerFunctions.http(transactionServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> notificationServiceRoute() {
        return GatewayRouterFunctions.route("notification_service")
                .route(RequestPredicates.path("/api/notification/**"), HandlerFunctions.http(notificationServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return GatewayRouterFunctions.route("user_service")
                .route(RequestPredicates.path("/api/user/**"), HandlerFunctions.http(userServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> paymentServiceRoute() {
        return GatewayRouterFunctions.route("payment_service")
                .route(RequestPredicates.path("/api/payment/**"), HandlerFunctions.http(paymentServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> messageServiceRoute() {
        return GatewayRouterFunctions.route("message_service")
                .route(RequestPredicates.path("/api/message/**"), HandlerFunctions.http(messageServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> componentServiceRoute() {
        return GatewayRouterFunctions.route("component_service")
                .route(RequestPredicates.path("/api/component/**"), HandlerFunctions.http(componentServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> pcBuilderServiceRoute() {
        return GatewayRouterFunctions.route("pc_builder_service")
                .route(RequestPredicates.path("/api/pc-builder/**"), HandlerFunctions.http(pcBuilderServiceUrl))
                .build();
    }
}
