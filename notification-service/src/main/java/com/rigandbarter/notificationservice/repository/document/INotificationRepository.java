package com.rigandbarter.notificationservice.repository.document;

import com.rigandbarter.notificationservice.model.Notification;

public interface INotificationRepository {
    Notification saveNotification(Notification notification);
}
