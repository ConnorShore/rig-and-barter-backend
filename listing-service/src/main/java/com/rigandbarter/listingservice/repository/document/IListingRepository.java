package com.rigandbarter.listingservice.repository.document;

import com.rigandbarter.listingservice.model.Listing;

import java.util.List;

public interface IListingRepository {

    Listing saveListing(Listing listing);

    List<Listing> getAllListings();
}
