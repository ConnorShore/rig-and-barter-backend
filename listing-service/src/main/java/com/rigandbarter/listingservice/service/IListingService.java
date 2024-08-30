package com.rigandbarter.listingservice.service;

import com.rigandbarter.listingservice.dto.ListingRequest;
import com.rigandbarter.core.models.ListingResponse;
import com.rigandbarter.listingservice.model.Listing;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IListingService {

    /**
     * Creates a new listing in the database and uploads images to blob storage
     * @param listingRequest The listing metadata to save to document db
     * @param images The listing's images to save to blob storage
     * @return The created listing
     */
    ListingResponse createListing(ListingRequest listingRequest, List<MultipartFile> images, Jwt principal);

    /**
     * Gets all the listings currently active
     * @return All active listings
     */
    List<ListingResponse> getAllListings();

    /**
     * Gets a specific listing by its id
     * @param listingId The id of the listing to retrieve
     * @return The listing with id equal to listingId
     */
    ListingResponse getListingById(String listingId);

    /**
     * Sets the listings for the user to be verified or not
     * @param userId The id of the user to get listings for
     * @param verified The verification status to set
     */
    void setVerificationForListings(String userId, boolean verified);

    /**
     * Deletes a specific listing by its id
     * @param listingId The id of the listing to delete
     * @param deleteTransaction True if should delete associated transactions
     * @param principal The auth principal of the user
     */
    void deleteListingById(String listingId, boolean deleteTransaction, Jwt principal);
}
