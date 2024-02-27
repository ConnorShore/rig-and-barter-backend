package com.rigandbarter.notificationservice.service;

import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.notificationservice.model.Notification;
import com.rigandbarter.notificationservice.model.notification.EmailNotification;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;
import com.rigandbarter.notificationservice.repository.document.INotificationRepository;
import com.rigandbarter.notificationservice.repository.mapper.FrontEndNotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final INotificationRepository notificationRepository;

    public void saveNotification(Notification notification) {
        notificationRepository.saveNotification(notification);
        log.info("Saved notification in repository");
    }

    public void initiateFrontendNotification(FrontEndNotification notification) {
        log.info("Front end notification sent (NOT_IMPLEMENTED_YET)");
    }

    public void initiateEmailNotification(EmailNotification notification) {
        log.info("Email sent (NOT_IMPLEMENTED_YET)");
    }
}
