package com.rigandbarter.notificationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service

public class WebSocketService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketService(final SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendMessage(String message) {
        simpMessagingTemplate.convertAndSend("/topic/tester", message);
    }
}
