package com.rigandbarter.notificationservice.repository.document.mongodb;

import com.rigandbarter.notificationservice.model.Notification;
import com.rigandbarter.notificationservice.repository.document.INotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(value = "rb.storage.document", havingValue = "mongodb")
@Slf4j
public class MongoDbListingRepositoryImpl extends SimpleMongoRepository<Notification, String> implements INotificationRepository {
    public MongoDbListingRepositoryImpl(MongoOperations mongoOperations) {
        super(new MongoRepositoryFactory(mongoOperations).getEntityInformation(Notification.class), mongoOperations);
    }

    @Override
    public Notification saveNotification(Notification notification) {
        return super.save(notification);
    }
}
