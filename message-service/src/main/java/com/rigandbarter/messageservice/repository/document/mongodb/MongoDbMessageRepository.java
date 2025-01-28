package com.rigandbarter.messageservice.repository.document.mongodb;

import com.rigandbarter.messageservice.model.MessageGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDbMessageRepository extends MongoRepository<MessageGroup, String> {
}
