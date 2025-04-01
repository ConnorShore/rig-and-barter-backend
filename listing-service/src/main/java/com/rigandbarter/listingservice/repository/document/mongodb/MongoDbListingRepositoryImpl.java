package com.rigandbarter.listingservice.repository.document.mongodb;

import com.rigandbarter.listingservice.model.Listing;
import com.rigandbarter.listingservice.repository.document.IListingRepository;
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
public class MongoDbListingRepositoryImpl extends SimpleMongoRepository<Listing, String> implements IListingRepository {

    private final MongoTemplate mongoTemplate;

    public MongoDbListingRepositoryImpl(MongoOperations mongoOperations, MongoTemplate mongoTemplate) {
        super(new MongoRepositoryFactory(mongoOperations).getEntityInformation(Listing.class), mongoOperations);
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Listing saveListing(Listing listing) {
        return super.save(listing);
    }

    @Override
    public List<Listing> saveListings(List<Listing> listings) {
        return super.saveAll(listings);
    }

    @Override
    public List<Listing> getAllListings() {
        return super.findAll();
    }

    @Override
    public List<Listing> getAllListingsForUser(String userId) {
        Criteria buyer = Criteria.where("userId").is(userId);
        Query query = new Query();
        query.addCriteria(buyer);
        return  mongoTemplate.find(query, Listing.class);
    }

    @Override
    public Listing getListingById(String listingId) {
        var listingOptional =  super.findById(listingId);
        return listingOptional.orElse(null);
    }

    @Override
    public void deleteListingById(String listingId) {
        super.deleteById(listingId);
    }

    @Override
    public void deleteListingsForUser(String userId) {
        Criteria user = Criteria.where("userId").is(userId);
        Query query = new Query();
        query.addCriteria(user);
        mongoTemplate.findAllAndRemove(query, Listing.class);
    }
}
