package com.rigandbarter.componentservice.repository.document.mongodb;

import com.rigandbarter.componentservice.model.Component;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDbComponentRepository extends MongoRepository<Component, String> {
}
