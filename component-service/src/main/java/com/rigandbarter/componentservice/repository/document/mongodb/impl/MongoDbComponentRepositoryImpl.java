package com.rigandbarter.componentservice.repository.document.mongodb.impl;

import com.rigandbarter.componentservice.model.Component;
import com.rigandbarter.componentservice.repository.document.IComponentRepository;
import com.rigandbarter.core.models.ComponentCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class MongoDbComponentRepositoryImpl extends SimpleMongoRepository<Component, String> implements IComponentRepository {

    private final MongoTemplate mongoTemplate;

    public MongoDbComponentRepositoryImpl(MongoOperations mongoOperations, MongoTemplate mongoTemplate) {
        super(new MongoRepositoryFactory(mongoOperations).getEntityInformation(Component.class), mongoOperations);
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Component> saveAllComponents(List<Component> components) {
        return super.saveAll(components);
    }

    @Override
    public List<Component> getAllComponents() {
        return super.findAll();
    }

    @Override
    public List<Component> getAllComponentsOfCategory(ComponentCategory category) {
        Query query = new Query();
        query.addCriteria(Criteria.where("category").is(category));
        return mongoTemplate.find(query,  Component.class);
    }

    @Override
    public List<Component> getPaginatedComponentsOfCategory(ComponentCategory category, int page, int numPerPage, String sortColumn, boolean descending) {
        Sort.Order order = new Sort.Order(descending ? Sort.Direction.DESC : Sort.Direction.ASC, sortColumn);
        Pageable paging = PageRequest.of(page, numPerPage, Sort.by(order));

        Query query = new Query();
        query.addCriteria(Criteria.where("category").is(category));
        query.with(paging);

        return mongoTemplate.find(query, Component.class);
    }
}
