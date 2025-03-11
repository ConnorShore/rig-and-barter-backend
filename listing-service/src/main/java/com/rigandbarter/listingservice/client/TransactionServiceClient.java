package com.rigandbarter.listingservice.client;


import com.rigandbarter.core.models.TransactionResponse;
import com.rigandbarter.core.models.UserResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface TransactionServiceClient {

    @DeleteExchange(value = "/api/transaction/{listingId}")
    void deleteListingTransaction(@PathVariable String listingId, @RequestHeader("Authorization") String bearerToken);

    @GetExchange(value = "/api/transaction/listing/{listingId}")
    List<TransactionResponse> getActiveTransactionsForListing(@PathVariable String listingId, @RequestHeader("Authorization") String bearerToken);
}
