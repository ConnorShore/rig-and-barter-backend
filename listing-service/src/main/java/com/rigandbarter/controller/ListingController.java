package com.rigandbarter.controller;

import com.rigandbarter.dto.ListingRequest;
import com.rigandbarter.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/listing")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createListing(@RequestPart(name = "listing") ListingRequest listingRequest,
                                @RequestPart(name = "image") MultipartFile image) {

        return "Successfully created listing!";
    }

    @PostMapping(value = "/testBlob")
    @ResponseStatus(HttpStatus.OK)
    public String createBlob(@RequestParam(name = "file") MultipartFile file) {
        listingService.testBlob(file);
        return "Successfully added blob";
    }

    @GetMapping(value = "/testDoc")
    @ResponseStatus(HttpStatus.OK)
    public String createDoc() {
        listingService.testDocument();
        return "Successfully created doc";
    }

//    @GetMapping(path = "/select")
//    @ResponseStatus(HttpStatus.OK)
//    public String selectListing() {
//        if(listingService.test().equals("true"))
//            return "Listing returned";
//        return "No listings returned";
//    }
}
