package com.rigandbarter.notificationservice.repository.document.mongodb;

import com.rigandbarter.notificationservice.model.Notification;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoDbListingRepository extends MongoRepository<Notification, String> {
}
