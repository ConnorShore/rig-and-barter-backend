package com.rigandbarter.listingservice.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.listingservice.controller.IListingController;
import com.rigandbarter.listingservice.dto.ListingRequest;
import com.rigandbarter.listingservice.dto.ListingResponse;
import com.rigandbarter.listingservice.service.IListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ListingControllerImpl implements IListingController {

    private final IListingService listingService;

    @Override
    public String createListing(Jwt principal, String listingRequest, MultipartFile[] images) throws JsonProcessingException {
        ListingRequest listingRequestObj = new ObjectMapper().readValue(listingRequest, ListingRequest.class);
        log.info("Creating new listing requested for user: " + principal.getId());
        return listingService.createListing(listingRequestObj, Arrays.asList(images), principal.getId()).getId();
    }

    @Override
    public List<ListingResponse> getAllListings() {
        log.info("Retrieving all listings");
        return listingService.getAllListings();
    }

    @Override
    public ListingResponse getListingById(String listingId) {
        log.info("Retrieving listing: " + listingId);
        return listingService.getListingById(listingId);
    }

    @Override
    public String healthCheck() {
        return "Listing Service is running...";
    }
}
