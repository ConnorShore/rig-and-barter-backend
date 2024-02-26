package com.rigandbarter.listingservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rigandbarter.eventservice.events.DemoEvent;
import com.rigandbarter.eventservice.events.TestEvent;
import com.rigandbarter.eventservice.components.RBEventProducer;
import com.rigandbarter.eventservice.components.RBEventProducerFactory;
import com.rigandbarter.listingservice.dto.ListingRequest;
import com.rigandbarter.listingservice.dto.ListingResponse;
import com.rigandbarter.listingservice.service.ListingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/listing")
@RequiredArgsConstructor
@Slf4j
public class ListingController {

    private final ListingService listingService;
    private final RBEventProducerFactory rbEventProducerFactory;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void createListing(@AuthenticationPrincipal Jwt principal,
                              @RequestPart(name = "listing") String listingRequest,
                              @RequestPart(name = "images") MultipartFile[] images) throws JsonProcessingException {
        ListingRequest listingRequestObj = new ObjectMapper().readValue(listingRequest, ListingRequest.class);

        log.info("Creating new listing requested for user: " + principal.getId());
        listingService.createListing(listingRequestObj, Arrays.asList(images), principal.getId());
    }

    /**
     * TODO: Refactor view listings to only return necessary info for the gallery card
     *      Only need to return one photo (and maybe return the base64 for the photo in the payload so frontend
     *      doesn't need to make request to the url)
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ListingResponse> getAllListings() {
        log.info("Retrieving all listings");
        return listingService.getAllListings();
    }

    @GetMapping("{listingId}")
    @ResponseStatus(HttpStatus.OK)
    public ListingResponse getListingById(@PathVariable String listingId) {
        log.info("Retrieving listing: " + listingId);
        return listingService.getListingById(listingId);
    }

    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    public String healthCheck() {
        TestEvent testEvent = TestEvent.builder()
                .userId("UserIDValue")
                .id(UUID.randomUUID().toString())
                .source(ListingService.class.getSimpleName())
                .creationDate(LocalDateTime.now())
                .additionalInfo("Here is a brand new event!")
                .build();

        RBEventProducer testProducer = rbEventProducerFactory.createProducer(TestEvent.class);
        testProducer.send(testEvent);

        DemoEvent demoEvent = DemoEvent.builder()
                .userId("UserIDValue")
                .id(UUID.randomUUID().toString())
                .source(ListingService.class.getSimpleName())
                .creationDate(LocalDateTime.now())
                .additionalNumber(12)
                .build();

        RBEventProducer demoProducer = rbEventProducerFactory.createProducer(DemoEvent.class);
        demoProducer.send(demoEvent);

        return "Listing Service is running...";
    }
}
