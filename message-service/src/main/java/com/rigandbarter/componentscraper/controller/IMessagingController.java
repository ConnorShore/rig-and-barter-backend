package com.rigandbarter.componentscraper.controller;

import com.rigandbarter.componentscraper.dto.MessageRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

public interface IMessagingController {

    @MessageMapping("/chat")
    void handleMessage(@Payload MessageRequest messageRequest);
}
