package com.rigandbarter.notificationservice.service;

import com.rigandbarter.notificationservice.model.Notification;
import com.rigandbarter.notificationservice.model.notification.EmailNotification;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;

public interface INotificationService {

    /**
     * Save the notification to the database
     * @param notification The notification to save
     */
    void saveNotification(Notification notification);

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