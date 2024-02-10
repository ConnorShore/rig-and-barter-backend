package com.rigandbarter.repository.document;

import com.rigandbarter.model.Listing;

public interface IListingRepository {

    Listing saveListing(Listing listing);
}
