package com.rigandbarter.listingservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.listingservice.dto.ListingRequest;
import com.rigandbarter.listingservice.service.ListingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/listing")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void createListing(@RequestPart(name = "listing") String listingRequest,
                              @RequestPart(name = "image") MultipartFile image) throws JsonProcessingException {
        ListingRequest listingRequestObj = new ObjectMapper().readValue(listingRequest, ListingRequest.class);
        listingService.createListing(listingRequestObj, image);
    }

    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    public String healthCheck() {
        return "Listing Service is running...";
    }
}
