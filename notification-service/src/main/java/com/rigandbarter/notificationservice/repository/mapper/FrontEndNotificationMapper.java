package com.rigandbarter.notificationservice.repository.mapper;

import com.rigandbarter.eventlibrary.model.RBEvent;
import com.rigandbarter.notificationservice.dto.FrontEndNotificationResponse;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotificationType;

import java.util.UUID;

public class FrontEndNotificationMapper {
    /**
     * Converts the event to a Front End Notification
     * @param event The event to convert
     * @param title The title (header) of the frontend notification
     * @param body The contents of the front end notification
     * @param actionUrl A url to add a button for to navigate to on the front end
     * @param notificationType The type of notification to display
     * @return The created frontend notification
     */
    public static FrontEndNotification fromEvent(RBEvent event, String title, String body, String actionUrl, FrontEndNotificationType notificationType) {
        return FrontEndNotification.builder()
                .id(UUID.randomUUID().toString())
                .eventId(event.getId())
                .targetUser(event.getUserId())
                .title(title)
                .body(body)
                .actionUrl(actionUrl)
                .notificationType(notificationType)
                .seenByUser(false)
                .creationDate(event.getCreationDate())
                .build();
    }

    /**
     * Converts FrontEndNotification to a response to be used by front end
     * @param notification The FrontEndNotification entity to convert
     * @return A FrontEndNotification to be sent to the front end
     */
    public static FrontEndNotificationResponse entityToDto(FrontEndNotification notification) {
        return FrontEndNotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .body(notification.getBody())
                .seenByUser(notification.isSeenByUser())
                .actionUrl(notification.getActionUrl())
                .notificationType(notification.getNotificationType())
                .creationDate(notification.getCreationDate())
                .build();
    }
}
