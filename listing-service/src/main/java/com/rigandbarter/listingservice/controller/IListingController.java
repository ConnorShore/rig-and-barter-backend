package com.rigandbarter.listingservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rigandbarter.listingservice.dto.ListingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/listing")
public interface IListingController {

    /**
     * Endpoint for creating a listing
     * @param principal The auth principal (user)
     * @param listingRequest The request object to create a listing from
     * @param images The list of images to include in the listing
     * @return The id of the created listing
     * @throws JsonProcessingException Exception when dealing with serialization
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    String createListing(@AuthenticationPrincipal Jwt principal,
                                 @RequestPart(name = "listing") String listingRequest,
                                 @RequestPart(name = "images") MultipartFile[] images) throws JsonProcessingException;

    /**
     * Get all listings
     * @return All listings
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    List<ListingResponse> getAllListings();

    /**
     * Gets a specific listing based on the id
     * @param listingId the id of the listing to retrieve
     * @return The listing if found, null if otherwise
     */
    @GetMapping("{listingId}")
    @ResponseStatus(HttpStatus.OK)
    ListingResponse getListingById(@PathVariable String listingId);

    /**
     * Status endpoint to see if service is running
     */
    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
