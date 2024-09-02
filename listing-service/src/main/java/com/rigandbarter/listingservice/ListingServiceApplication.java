package com.rigandbarter.listingservice;

import com.rigandbarter.eventlibrary.EnableRBEvents;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRBEvents
public class ListingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ListingServiceApplication.class, args);
    }
}