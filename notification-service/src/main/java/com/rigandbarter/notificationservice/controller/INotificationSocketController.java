package com.rigandbarter.notificationservice.controller;

import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

public interface INotificationSocketController {

    @MessageMapping("/notification")
    void handleNotification(@Payload FrontEndNotification messageRequest);
}
