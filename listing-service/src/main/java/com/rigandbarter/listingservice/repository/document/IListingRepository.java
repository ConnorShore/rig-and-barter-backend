package com.rigandbarter.listingservice.repository.document;

import com.rigandbarter.listingservice.model.Listing;

import java.util.List;

public interface IListingRepository {

    /**
     * Save/update the listing in the database
     * @param listing The listing to save
     * @return The saved listing, null if failed
     */
    Listing saveListing(Listing listing);

    /**
     * Saves/updates a list of listings to the db
     * @param listings The listings to save/update
     * @return The updated listings
     */
    List<Listing> saveListings(List<Listing> listings);

    /**
     * Retrieves all listings in the database
     * @return All listings, null if failed
     */
    List<Listing> getAllListings();

    /**
     * Retrieves all listings for a specific user
     * @param userId The id of the user to get listings for
     * @return All of the user's listings
     */
    List<Listing> getAllListingsForUser(String userId);

    /**
     * Retrieves a specific listing based on its id
     * @param listingId The id of the listing to retrieve
     * @return The listing if found, null if otherwise
     */
    Listing getListingById(String listingId);

    /**
     * Deletes a specific listing based on its id
     * @param listingId The id of the listing to delete
     */
    void deleteListingById(String listingId);
}
