package com.rigandbarter.messageservice.repository.document.mongodb;

import com.rigandbarter.messageservice.model.Message;
import com.rigandbarter.messageservice.model.MessageGroup;
import com.rigandbarter.messageservice.repository.document.IMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@ConditionalOnProperty(value = "rb.storage.document", havingValue = "mongodb")
@Slf4j
public class MongoDbMessageRepositoryImpl extends SimpleMongoRepository<MessageGroup, String> implements IMessageRepository {

    private final MongoTemplate mongoTemplate;

    public MongoDbMessageRepositoryImpl(MongoOperations mongoOperations, MongoTemplate mongoTemplate) {
        super(new MongoRepositoryFactory(mongoOperations).getEntityInformation(MessageGroup.class), mongoOperations);
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public MessageGroup saveMessageGroup(MessageGroup group) {
        return super.save(group);
    }

    @Override
    public Message saveMessageToGroup(String groupId, Message message) {
        MessageGroup group = super.findById(groupId).orElse(null);
        if(group == null) {
            log.warn("Failed to find group with id: " + groupId);
            return null;
        }

        group.addMessage(message);
        super.save(group);
        return message;
    }

    @Override
    public void deleteMessageFromGroup(String groupId, String messageId) {
        MessageGroup group = super.findById(groupId).orElse(null);
        if(group == null) {
            log.warn("Failed to find group with id: " + groupId);
            return;
        }

        group.deleteMessage(messageId);
        super.save(group);
    }

    @Override
    public void deleteNotificationGroup(String messageGroupId) {
        super.deleteById(messageGroupId);
    }

    @Override
    public List<MessageGroup> getAllMessageGroupsForUser(String userId) {
        Criteria buyer = Criteria.where("buyerId").is(userId);
        Criteria seller = Criteria.where("sellerId").is(userId);
        Criteria combined = buyer.orOperator(seller);
        Query query = new Query();
        query.addCriteria(combined);
        return mongoTemplate.find(query,  MessageGroup.class);
    }
}
