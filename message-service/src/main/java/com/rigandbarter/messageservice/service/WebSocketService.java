package com.rigandbarter.messageservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Value("${rb.websocket.topic.messaging}")
    private String MESSAGING_TOPIC;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketService(final SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

//    public void sendFrontendMessage(MessageFrontend notification) {
//        String topic = String.format("/topic/%s", MESSAGING_TOPIC);
//        simpMessagingTemplate.convertAndSend(topic, notification);
//    }
}
