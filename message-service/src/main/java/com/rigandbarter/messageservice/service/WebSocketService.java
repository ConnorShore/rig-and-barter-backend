package com.rigandbarter.messageservice.service;

import com.rigandbarter.messageservice.dto.MessageRequest;
import com.rigandbarter.messageservice.dto.MessageResponse;
import com.rigandbarter.messageservice.service.impl.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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

    public void sendDirectedFrontendMessage(MessageResponse message, String topic) {
        String fullTopic = String.format("/mtopic/%s", topic);
        simpMessagingTemplate.convertAndSend(fullTopic, message);
    }

    public void sendGeneralFrontendMessage(MessageResponse message) {
        String topic = String.format("/mtopic/%s", MESSAGING_TOPIC);
        simpMessagingTemplate.convertAndSend(topic, message);
    }
}
