package com.rigandbarter.messageservice.service.impl;

import com.rigandbarter.core.models.ListingResponse;
import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.messageservice.dto.MessageGroupRequest;
import com.rigandbarter.messageservice.dto.MessageGroupResponse;
import com.rigandbarter.messageservice.service.IEventHandlerService;
import com.rigandbarter.messageservice.service.IMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventHandlerServiceImpl implements IEventHandlerService {

    private final IMessageService messageService;
    private final WebClient.Builder webClientBuilder;

    @Override
    public RBResultStatus<Void> handleTransactionCreatedEvent(TransactionCreatedEvent event) {
        ListingResponse listingResponse = webClientBuilder.build()
                .get()
                .uri("http://listing-service/api/listing/{listingId}", event.getListingId())
                .retrieve()
                .bodyToMono(ListingResponse.class)
                .block();

        if (listingResponse == null) {
            String msg = "Failed to retrieve listing from listing service: " + event.getListingId();
            log.error(msg);
            return new RBResultStatus<>(false, msg);
        }

        MessageGroupRequest messageGroup =  MessageGroupRequest.builder()
                .buyerId(event.getBuyerId())
                .sellerId(event.getSellerId())
                .groupName(listingResponse.getTitle() + " - Group")
                .groupImageUrl(
                        listingResponse.getImageUrls()
                                .stream()
                                .findFirst()
                                .orElse(null)
                )
                .build();

        MessageGroupResponse createdGroup = messageService.createMessageGroup(messageGroup);
        if(createdGroup == null)
            return new RBResultStatus<>(false, "Failed to create message group");

        return new RBResultStatus<>(true);
    }
}
