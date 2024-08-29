package com.rigandbarter.notificationservice.service;

import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotificationType;
import com.rigandbarter.notificationservice.repository.mapper.FrontEndNotificationMapper;
import com.rigandbarter.notificationservice.service.impl.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    /**
     * Add more topics for different types
     */
    @Value("${rb.websocket.topic.front-end}")
    private String FRONT_END_TOPIC;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketService(final SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendFrontendMessage(FrontEndNotification notification) {
        String topic = String.format("/topic/%s", FRONT_END_TOPIC) ;
        simpMessagingTemplate.convertAndSend(topic, notification);
    }
}
