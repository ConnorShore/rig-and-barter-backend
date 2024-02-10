package com.rigandbarter.repository;

import com.rigandbarter.model.Listing;

public interface IListingRepository {

    Listing saveListing(Listing listing);
}
