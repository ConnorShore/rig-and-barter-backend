package com.rigandbarter.notificationservice.service.impl;

import com.rigandbarter.notificationservice.dto.FrontEndNotificationResponse;
import com.rigandbarter.notificationservice.model.Notification;
import com.rigandbarter.notificationservice.model.notification.EmailNotification;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;
import com.rigandbarter.notificationservice.repository.document.INotificationRepository;
import com.rigandbarter.notificationservice.repository.mapper.FrontEndNotificationMapper;
import com.rigandbarter.notificationservice.service.INotificationService;
import com.rigandbarter.notificationservice.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements INotificationService {

    private final INotificationRepository notificationRepository;
    private final WebSocketService webSocketService;

    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.saveNotification(notification);
        log.info("Saved notification in repository");
    }

    @Override
    public void deleteNotification(String notificationId) {
        notificationRepository.deleteNotification(notificationId);
        log.info("Deleted notification: " + notificationId);
    }

    @Override
    public void markNotificationAsSeen(String notificationId) {
        notificationRepository.markNotificationAsSeen(notificationId);
    }

    @Override
    public void markAllUserNotificationsAsSeen(String userId) {
        notificationRepository.markAllUserNotificationsAsSeen(userId);
    }

    @Override
    public List<FrontEndNotificationResponse> getAllNotificationsForUser(String userId) {
        List<Notification> userNotifications = notificationRepository.getAllFrontEndNotificationsForUser(userId);
        return userNotifications
                .stream()
                .map(n -> (FrontEndNotification)n)
                .map(FrontEndNotificationMapper::entityToDto)
                .toList();
    }

    @Override
    public void initiateFrontEndNotification(FrontEndNotification notification) {
        webSocketService.sendFrontendMessage(notification);
    }

    @Override
    public void initiateEmailNotification(EmailNotification notification) {
        log.info("Email sent (NOT_IMPLEMENTED_YET)");
    }
}
