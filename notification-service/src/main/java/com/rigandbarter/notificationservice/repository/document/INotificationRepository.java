package com.rigandbarter.notificationservice.repository.document;

import com.rigandbarter.notificationservice.model.Notification;

public interface INotificationRepository {

    /**
     * Saves notification to notification database
     * @param notification The notification to save
     * @return The notification if saved, null otherwise
     */
    Notification saveNotification(Notification notification);
}
