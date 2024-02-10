package com.rigandbarter.listingservice.repository.document;

import com.rigandbarter.listingservice.model.Listing;

public interface IListingRepository {

    Listing saveListing(Listing listing);
}
