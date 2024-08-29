package com.rigandbarter.notificationservice.controller.impl;

import com.rigandbarter.notificationservice.controller.INotificationSocketController;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;
import com.rigandbarter.notificationservice.service.INotificationService;
import com.rigandbarter.notificationservice.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class NotificationSocketControllerImpl implements INotificationSocketController {

    private final WebSocketService webSocketService;
    private final INotificationService notificationService;

    @Override
    public void handleNotification(FrontEndNotification notification) {
        notification.setCreationDate(LocalDateTime.now());
        notificationService.saveNotification(notification);
        webSocketService.sendFrontendMessage(notification);
    }
}
