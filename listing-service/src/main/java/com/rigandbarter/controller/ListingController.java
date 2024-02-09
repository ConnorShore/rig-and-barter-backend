package com.rigandbarter.controller;

import com.rigandbarter.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/listing")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getAllListings() {
        return "All listings returned";
    }


    @GetMapping(path = "/select")
    @ResponseStatus(HttpStatus.OK)
    public String selectListing() {
        if(listingService.selectListing().equals("true"))
            return "Listing returned";
        return "No listings returned";
    }
}
