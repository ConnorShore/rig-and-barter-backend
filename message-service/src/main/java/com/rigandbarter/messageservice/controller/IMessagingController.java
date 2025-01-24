package com.rigandbarter.messageservice.controller;

import com.rigandbarter.messageservice.dto.MessageRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

public interface IMessagingController {

    @MessageMapping("/chat")
    void handleMessage(@Payload MessageRequest messageRequest);
}
