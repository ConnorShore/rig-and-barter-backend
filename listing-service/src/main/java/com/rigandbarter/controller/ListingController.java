package com.rigandbarter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/listing")
public class ListingController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getAllListings() {
        return "All listings returned";
    }
}
