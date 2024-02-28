package com.rigandbarter.notificationservice.repository.mapper;

import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.notificationservice.model.notification.EmailNotification;

import java.util.UUID;

public class EmailNotificationMapper {

    /**
     * Converts the event to an Email Notification
     * @param event The event to convert
     * @param subject The subject of the email
     * @param body the body of the email
     * @return The created email notification
     */
    public static EmailNotification fromEvent(RBEvent event, String subject, String body) {
        return EmailNotification.builder()
                .id(UUID.randomUUID().toString())
                .eventId(event.getId())
                .targetUser(event.getUserId())
                .subject(subject)
                .body(body)
                .build();
    }
}
