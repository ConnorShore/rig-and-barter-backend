package com.rigandbarter.notificationservice.service.impl;

import com.rigandbarter.notificationservice.model.Notification;
import com.rigandbarter.notificationservice.model.notification.EmailNotification;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;
import com.rigandbarter.notificationservice.repository.document.INotificationRepository;
import com.rigandbarter.notificationservice.service.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements INotificationService {

    private final INotificationRepository notificationRepository;

    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.saveNotification(notification);
        log.info("Saved notification in repository");
    }

    @Override
    public void initiateFrontEndNotification(FrontEndNotification notification) {
        log.info("Front end notification sent (NOT_IMPLEMENTED_YET)");
    }

    @Override
    public void initiateEmailNotification(EmailNotification notification) {
        log.info("Email sent (NOT_IMPLEMENTED_YET)");
    }
}
