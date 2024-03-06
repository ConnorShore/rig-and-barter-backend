package com.rigandbarter.notificationservice.repository.document.mongodb;

import com.rigandbarter.notificationservice.model.Notification;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;
import com.rigandbarter.notificationservice.repository.document.INotificationRepository;
import com.rigandbarter.notificationservice.repository.mapper.FrontEndNotificationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ConditionalOnProperty(value = "rb.storage.document", havingValue = "mongodb")
@Slf4j
public class MongoDbListingRepositoryImpl extends SimpleMongoRepository<Notification, String> implements INotificationRepository {
    private final MongoTemplate mongoTemplate;

    public MongoDbListingRepositoryImpl(MongoOperations mongoOperations, MongoTemplate mongoTemplate) {
        super(new MongoRepositoryFactory(mongoOperations).getEntityInformation(Notification.class), mongoOperations);
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Notification saveNotification(Notification notification) {
        return super.save(notification);
    }

    @Override
    public List<Notification> getAllFrontEndNotificationsForUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("targetUser").is(userId));
        return mongoTemplate.find(query,  Notification.class);
    }

    @Override
    public void deleteNotification(String notificationId) {
        super.deleteById(notificationId);
    }
}
