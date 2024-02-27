package com.rigandbarter.notificationservice.service;

import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.eventlibrary.model.RBEventResult;
import com.rigandbarter.notificationservice.dto.ListingResponse;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;
import com.rigandbarter.notificationservice.repository.mapper.FrontEndNotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventHandlerService {

    private final NotificationService notificationService;
    private final WebClient.Builder webClientBuilder;

    private final String FRONT_END_NOTIFICATION_TITLE = "New Transaction Request!";
    private final String FRONT_END_NOTIFICATION_BODY = "User [%s] is interested in purchasing: %s";

    /**
     * TODO: See if this is good practice or if this should just be in NotificationService.java
     */
    public RBEventResult handleTransactionCreatedEvent(TransactionCreatedEvent event) {
        /**
         * TODO: Need to add a common library with ways to access Auth object (like buyer and sellers's name, email, etc)
         */
        ListingResponse listingResponse = webClientBuilder.build()
                .get()
                .uri("http://listing-service/api/listing/{listingId}", event.getListingId())
                .retrieve()
                .bodyToMono(ListingResponse.class)
                .block();

        if (listingResponse == null) {
            String msg = "Failed to retrieve listing from listing service: " + event.getListingId();
            log.error(msg);
            return new RBEventResult(false, msg);
        }

        FrontEndNotification frontEndNotification = FrontEndNotificationMapper.fromEvent(
                    event,
                    FRONT_END_NOTIFICATION_TITLE,
                    String.format(FRONT_END_NOTIFICATION_BODY, "Test Person", listingResponse.getTitle())
        );

        notificationService.saveNotification(frontEndNotification);
        notificationService.initiateFrontendNotification(frontEndNotification);

        // Create and send email notification
        // Etc

        return new RBEventResult(true);
    }
}
