package com.rigandbarter.listingservice.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.core.models.TransactionResponse;
import com.rigandbarter.listingservice.controller.IListingController;
import com.rigandbarter.listingservice.dto.ListingRequest;
import com.rigandbarter.core.models.ListingResponse;
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
    public ListingResponse createListing(Jwt principal, String listingRequest, MultipartFile[] images) throws JsonProcessingException {
        ListingRequest listingRequestObj = new ObjectMapper().readValue(listingRequest, ListingRequest.class);
        log.info("Creating new listing requested for user: " + principal.getSubject());
        return listingService.createListing(listingRequestObj, Arrays.asList(images), principal);
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
    public void deleteListingById(String listingId, boolean deleteTransaction, Jwt principal) {
        listingService.deleteListingById(listingId, deleteTransaction, principal.getTokenValue());
    }

    @Override
    public void updateListingPrice(String listingId, double price, Jwt principal) {
        listingService.updateListingPrice(listingId, price, principal.getTokenValue());
    }

    @Override
    public List<TransactionResponse> getActiveTransactionsForListing(String listingId, Jwt principal) {
        return listingService.getActiveTransactionsForListing(listingId, principal.getTokenValue());
    }

    @Override
    public String healthCheck() {
        return "Listing Service is running...";
    }
}
