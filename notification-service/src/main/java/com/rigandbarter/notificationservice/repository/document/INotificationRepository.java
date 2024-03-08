package com.rigandbarter.notificationservice.repository.document;

import com.rigandbarter.notificationservice.model.Notification;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;

import java.util.List;

public interface INotificationRepository {

    /**
     * Saves notification to notification database
     * @param notification The notification to save
     * @return The notification if saved, null otherwise
     */
    Notification saveNotification(Notification notification);

    /**
     * Returns all notifications for the current user
     * @param userId The user to retrieve notificaitons for
     * @return All notifications for the user
     */
    List<Notification> getAllFrontEndNotificationsForUser(String userId);

    /**
     * Deletes the specified notifications from the db
     * @param notificationIds The list of ids for the notifications to delete
     */
    void deleteNotifications(List<String> notificationIds);

    /**
     * Marks the frontend notification as Seen By User
     * @param notificationId The id of the notification to mark as seen
     */
    void markNotificationAsSeen(String notificationId);

    /**
     * Marks all frontend notifications as seen for the current user
     * @param userId The id of the user for whom to mark all notifications as seen for
     */
    void markAllUserNotificationsAsSeen(String userId);
}
