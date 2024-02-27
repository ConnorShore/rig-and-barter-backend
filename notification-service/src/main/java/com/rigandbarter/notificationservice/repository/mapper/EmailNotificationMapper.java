package com.rigandbarter.notificationservice.repository.mapper;

import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.notificationservice.model.notification.EmailNotification;

import java.util.UUID;

public class EmailNotificationMapper {

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
