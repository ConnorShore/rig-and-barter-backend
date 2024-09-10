package com.rigandbarter.pcbuilderservice.repository.mongodb.impl;

import com.rigandbarter.pcbuilderservice.model.PCBuild;
import com.rigandbarter.pcbuilderservice.repository.IPCBuilderRepository;
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
public class MongoDbPCBuilderRepositoryImpl extends SimpleMongoRepository<PCBuild, String> implements IPCBuilderRepository {

    private final MongoTemplate mongoTemplate;

    public MongoDbPCBuilderRepositoryImpl(MongoOperations mongoOperations, MongoTemplate mongoTemplate) {
        super(new MongoRepositoryFactory(mongoOperations).getEntityInformation(PCBuild.class), mongoOperations);
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public PCBuild save(PCBuild entity) {
        return super.save(entity);
    }

    @Override
    public List<PCBuild> getByUserID(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, PCBuild.class);
    }

    @Override
    public void deleteById(String id) {
        super.deleteById(id);
    }
}