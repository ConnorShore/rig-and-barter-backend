package com.rigandbarter.repository;

import com.rigandbarter.model.Listing;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;


@Repository
@ConditionalOnProperty(value = "rb.storage.document", havingValue = "test")
public class TestListingRepository implements IListingRepository {
    @Override
    public Listing saveListing(Listing listing) {
        System.out.println("Saved listing: " + listing);
        return null;
    }
}
