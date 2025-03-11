package com.rigandbarter.notificationservice.repository.document.mongodb;

import com.rigandbarter.notificationservice.model.Notification;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;
import com.rigandbarter.notificationservice.repository.document.INotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Repository;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(value = "rb.storage.document", havingValue = "mongodb")
@Slf4j
public class MongoDbNotificationRepositoryImpl extends SimpleMongoRepository<Notification, String> implements INotificationRepository {
    private final MongoTemplate mongoTemplate;

    public MongoDbNotificationRepositoryImpl(MongoOperations mongoOperations, MongoTemplate mongoTemplate) {
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
    public void deleteNotifications(List<String> notificationIds) {
        super.deleteAllById(notificationIds);
    }

    @Override
    public void deleteNotificationsForUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("targetUser").is(userId));
        mongoTemplate.remove(query);
    }

    @Override
    public void markNotificationAsSeen(String notificationId) {
        Optional<Notification> notificationOptional = super.findById(notificationId);
        if(notificationOptional.isEmpty())
            throw new NotFoundException(String.format("The notification with id [%s] was not found in the db", notificationId));

        FrontEndNotification frontEndNotification = (FrontEndNotification) notificationOptional.get();
        frontEndNotification.setSeenByUser(true);
        super.save(frontEndNotification);
    }

    @Override
    public void markAllUserNotificationsAsSeen(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("targetUser").is(userId));

        Update updateSeen = new Update();
        updateSeen.set("seenByUser", true);

        mongoTemplate.updateMulti(query, updateSeen, Notification.class);
    }
}
