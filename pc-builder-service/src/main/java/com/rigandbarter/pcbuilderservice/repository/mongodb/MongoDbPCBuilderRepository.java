package com.rigandbarter.pcbuilderservice.repository.mongodb;

import com.rigandbarter.pcbuilderservice.model.PCBuild;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDbPCBuilderRepository extends MongoRepository<PCBuild, String> {
}
