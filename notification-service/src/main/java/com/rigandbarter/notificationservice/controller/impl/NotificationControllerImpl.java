package com.rigandbarter.notificationservice.controller.impl;

import com.rigandbarter.notificationservice.controller.INotificationController;
import com.rigandbarter.notificationservice.dto.FrontEndNotificationResponse;
import com.rigandbarter.notificationservice.service.INotificationService;
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

    @Override
    public List<FrontEndNotificationResponse> getNotificationsForUser(Jwt principal) {
        return notificationService.getAllNotificationsForUser(principal.getSubject());
    }

    @Override
    public void deleteNotification(String notificationId) {
        notificationService.deleteNotification(notificationId);
    }

    @Override
    public void markNotificationAsSeen(String notificationId) {
        notificationService.markNotificationAsSeen(notificationId);
    }

    @Override
    public String healthCheck() {
        return "Notification service is running...";
    }
}
