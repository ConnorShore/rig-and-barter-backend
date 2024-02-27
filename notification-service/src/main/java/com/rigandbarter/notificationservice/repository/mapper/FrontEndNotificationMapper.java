package com.rigandbarter.notificationservice.repository.mapper;

import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;

import java.util.UUID;

public class FrontEndNotificationMapper {

    public static FrontEndNotification fromEvent(RBEvent event, String title, String body) {
        return FrontEndNotification.builder()
                .id(UUID.randomUUID().toString())
                .eventId(event.getId())
                .targetUser(event.getUserId())
                .title(title)
                .body(body)
                .seenByUser(false)
                .build();
    }
}
