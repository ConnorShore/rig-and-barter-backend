package com.rigandbarter.notificationservice.service.impl;

import com.rigandbarter.core.models.ListingResponse;
import com.rigandbarter.core.models.RBResultStatus;
import com.rigandbarter.eventlibrary.events.TransactionCreatedEvent;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotification;
import com.rigandbarter.notificationservice.model.notification.FrontEndNotificationType;
import com.rigandbarter.notificationservice.repository.mapper.FrontEndNotificationMapper;
import com.rigandbarter.notificationservice.service.IEventHandlerService;
import com.rigandbarter.notificationservice.service.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventHandlerServiceImpl implements IEventHandlerService {

    private final INotificationService notificationService;
    private final WebClient.Builder webClientBuilder;

    private final String FRONT_END_NOTIFICATION_TITLE = "New Transaction Request!";

    // TODO: Move this to an html template class maybe?
    private final String FRONT_END_NOTIFICATION_BODY = "User \"%s\" is interested in purchasing: %s";

    //TODO: Get this from config file
    private String FRONTEND_LISTING_URL = "http://localhost:4200/listings/";

    public RBResultStatus<Void> handleTransactionCreatedEvent(TransactionCreatedEvent event) {
        /**
         * TODO: Get user information from JWT decode or from principal on
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
            return new RBResultStatus<>(false, msg);
        }

        // TODO: Get name of actual buyer instead of Test Person
        FrontEndNotification frontEndNotification = FrontEndNotificationMapper.fromEvent(
                    event,
                    FRONT_END_NOTIFICATION_TITLE,
                    String.format(FRONT_END_NOTIFICATION_BODY,
                            "Test Person",
                            listingResponse.getTitle()
                    ),
                FRONTEND_LISTING_URL + listingResponse.getId(),
                    FrontEndNotificationType.INFO
        );

        notificationService.saveNotification(frontEndNotification);
        notificationService.sendFrontEndNotification(frontEndNotification);

        // Create and send email notification
        // Etc

        return new RBResultStatus<Void>(true);
    }
}
