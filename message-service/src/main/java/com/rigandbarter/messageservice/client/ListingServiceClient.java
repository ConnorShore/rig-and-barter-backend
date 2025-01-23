package com.rigandbarter.messageservice.client;

import com.rigandbarter.core.models.ListingResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;


public interface ListingServiceClient {

    @GetExchange(value = "/api/listing/{listingId}")
    ListingResponse getListing(@PathVariable String listingId);
}
