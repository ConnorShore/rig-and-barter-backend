package com.rigandbarter.listingservice.repository.document.mongodb;

import com.rigandbarter.listingservice.model.Listing;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDbListingRepository extends MongoRepository<Listing, String> {
}
