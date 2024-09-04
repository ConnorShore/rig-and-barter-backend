package com.rigandbarter.componentscraper.repository.document.mongodb;

import com.rigandbarter.componentscraper.model.MessageGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDbMessageRepository extends MongoRepository<MessageGroup, String> {
}
