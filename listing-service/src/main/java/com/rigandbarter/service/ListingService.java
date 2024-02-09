package com.rigandbarter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final WebClient.Builder webClientBuilder;

    public String selectListing() {

        String ret = webClientBuilder.build().get()
                .uri("http://transaction-service/api/transaction/test")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ret;
    }

}
