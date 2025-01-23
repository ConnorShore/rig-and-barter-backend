package com.rigandbarter.notificationservice.controller.impl;

import com.rigandbarter.notificationservice.controller.INotificationController;
import com.rigandbarter.notificationservice.dto.FrontEndNotificationResponse;
import com.rigandbarter.notificationservice.model.NotificationType;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotificationType;
import com.rigandbarter.notificationservice.service.INotificationService;
import com.rigandbarter.notificationservice.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationControllerImpl implements INotificationController {

    private final INotificationService notificationService;
    private final WebSocketService websocketService;

    @Override
    public List<FrontEndNotificationResponse> getNotificationsForUser(Jwt principal) {
        return notificationService.getAllNotificationsForUser(principal.getSubject());
    }

    @Override
    public void deleteNotifications(List<String> notificationIds) {
        notificationService.deleteNotifications(notificationIds);
    }

    @Override
    public void markNotificationAsSeen(String notificationId) {
        notificationService.markNotificationAsSeen(notificationId);
    }

    @Override
    public void markAllUserNotificationAsSeen(Jwt principal) {
        notificationService.markAllUserNotificationsAsSeen(principal.getSubject());
    }

    @Override
    public String healthCheck() {
        websocketService.sendFrontendMessage(FrontEndNotification.builder()
                .type(NotificationType.FRONT_END_NOTIFICATION)
                .notificationType(FrontEndNotificationType.INFO)
                .body("Notification service is running...")
                .build());
//        notificationService.sendFrontEndNotification(FrontEndNotification.builder()
//                .type(NotificationType.FRONT_END_NOTIFICATION)
//                .body("Notification service is running...")
//                .build());

        return "Notification service is running...";
    }
}
