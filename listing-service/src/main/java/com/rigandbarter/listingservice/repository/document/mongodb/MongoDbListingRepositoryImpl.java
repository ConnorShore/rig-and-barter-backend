package com.rigandbarter.listingservice.repository.document.mongodb;

import com.rigandbarter.listingservice.model.Listing;
import com.rigandbarter.listingservice.repository.document.IListingRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(value = "rb.storage.document", havingValue = "mongodb")
public class MongoDbListingRepositoryImpl extends SimpleMongoRepository<Listing, String> implements IListingRepository {
    public MongoDbListingRepositoryImpl(MongoOperations mongoOperations) {
        super(new MongoRepositoryFactory(mongoOperations).getEntityInformation(Listing.class), mongoOperations);
    }

    @Override
    public Listing saveListing(Listing listing) {
        return super.save(listing);
    }
}