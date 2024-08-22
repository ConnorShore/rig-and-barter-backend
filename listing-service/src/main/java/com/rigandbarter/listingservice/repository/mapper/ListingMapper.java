package com.rigandbarter.listingservice.repository.mapper;

import com.rigandbarter.core.models.UserBasicInfo;
import com.rigandbarter.listingservice.dto.ListingRequest;
import com.rigandbarter.listingservice.dto.ListingResponse;
import com.rigandbarter.listingservice.model.Listing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ListingMapper {

    /**
     * Mapper for Listing to ListingResponse
     * @param listing The db Listing item
     * @return A ListingResponse object
     */
    public static ListingResponse entityToDto(Listing listing) {
        return ListingResponse.builder()
                .id(listing.getId())
                .userId(listing.getUserId())
                .title(listing.getTitle())
                .description(listing.getDescription())
                .price(listing.getPrice())
                .creationDate(listing.getCreationDate())
                .componentCategory(listing.getComponentCategory())
                .imageUrls(listing.getImageUrls())
                .userInfo(listing.getUserInfo())
                .build();
    }

    /**
     * Mapper for ListingRequest to Listing
     * @param listingRequest The initial listing request
     * @param userId The user id of the request
     * @param stripeId The id of the product in stripe
     * @param imageUrls The urls to the listing's images
     * @param userInfo The basic user info for the listing user
     * @return The created listing
     */
    public static Listing dtoToEntity(ListingRequest listingRequest, String userId,
                                      String stripeId, List<String> imageUrls, UserBasicInfo userInfo) {
        return Listing.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .title(listingRequest.getTitle())
                .description(listingRequest.getDescription())
                .price(listingRequest.getPrice())
                .stripeId(stripeId)
                .creationDate(LocalDateTime.now())
                .componentCategory(listingRequest.getComponentCategory())
                .imageUrls(imageUrls)
                .userInfo(userInfo)
                .build();
    }
}
