package com.rigandbarter.notificationservice.service;

import com.rigandbarter.notificationservice.dto.FrontEndNotificationResponse;
import com.rigandbarter.notificationservice.model.Notification;
import com.rigandbarter.notificationservice.model.notification.EmailNotification;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;

import java.util.List;

public interface INotificationService {

    /**
     * Save the notification to the database
     * @param notification The notification to save
     */
    void saveNotification(Notification notification);

    /**
     * Delete the notification to the database
     * @param notificationId The id of the notification to delete
     */
    void deleteNotification(String notificationId);

    /**
     * Gets all front end notifications for the specified user
     * @param userId User id for whom to get notifications for
     * @return All active notifications for the user
     */
    List<FrontEndNotificationResponse> getAllNotificationsForUser(String userId);

    /**
     * Sends notification to the front end
     * @param notification The notification to send
     */
    void initiateFrontEndNotification(FrontEndNotification notification);

    /**
     * Sends notification out via email
     * @param notification The notification to send
     */
    void initiateEmailNotification(EmailNotification notification);
}
