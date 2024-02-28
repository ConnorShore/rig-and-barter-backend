package com.rigandbarter.notificationservice.repository.mapper;

import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;

import java.util.UUID;

public class FrontEndNotificationMapper {

    /**
     * Converts the event to a Front End Notification
     * @param event The event to convert
     * @param title The title (header) of the frontend notification
     * @param body The contents of the front end notification
     * @return The created frontend notification
     */
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
