package com.rigandbarter.repository.document.mongodb;

import com.rigandbarter.model.Listing;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDbListingRepository extends MongoRepository<Listing, String> {
}
