package com.rigandbarter.notificationservice.repository.document.mongodb;

import com.rigandbarter.notificationservice.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDbListingRepository extends MongoRepository<Notification, String> {
}
