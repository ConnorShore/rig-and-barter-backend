package com.rigandbarter.listingservice.service.impl;

import com.rigandbarter.core.models.UserBasicInfoPublic;
import com.rigandbarter.listingservice.dto.ListingRequest;
import com.rigandbarter.listingservice.dto.ListingResponse;
import com.rigandbarter.listingservice.dto.StripeProductRequest;
import com.rigandbarter.listingservice.model.Listing;
import com.rigandbarter.listingservice.repository.document.IListingRepository;
import com.rigandbarter.listingservice.repository.file.IFileRepository;
import com.rigandbarter.listingservice.repository.mapper.ListingMapper;
import com.rigandbarter.listingservice.service.IListingService;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingServiceImpl implements IListingService {

    private final IListingRepository listingRepository;
    private final IFileRepository fileRepository;

    private final WebClient.Builder webClientBuilder;

    @Override
    public Listing createListing(ListingRequest listingRequest, List<MultipartFile> images, Jwt principal) {
        String userId = principal.getSubject();

        // Get the info for the user
        UserBasicInfoPublic userBasicInfo = webClientBuilder.build()
                .get()
                .uri("http://user-service/api/user/" + userId + "/info/basic")
                .headers(h -> h.setBearerAuth(principal.getTokenValue()))
                .retrieve()
                .bodyToMono(UserBasicInfoPublic.class)
                .block();

        // Create a product in stripe for the listing
        StripeProductRequest stripeProductRequest = StripeProductRequest.builder()
                .name(listingRequest.getTitle())
                .description(listingRequest.getDescription())
                .productPrice(listingRequest.getPrice())
                .build();

        String productId = webClientBuilder.build()
                .post()
                .uri("http://payment-service/api/payment/product")
                .headers(h -> h.setBearerAuth(principal.getTokenValue()))
                .bodyValue(stripeProductRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (productId == null) {
            String msg = "Failed to create product in Stripe: " + listingRequest.getTitle();
            log.error(msg);

            //todo: maybe throw exception here or something
            return null;
        }

        // Save the listing image to file service
        List<String> imageUrls = new ArrayList<>();
        for(MultipartFile image : images) {
            var fileExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());
            var key = UUID.randomUUID() + "." + fileExtension;
            imageUrls.add(fileRepository.uploadFile(key, image));
        }

        // Save the listing data to the document db
        Listing listing = listingRepository.saveListing(ListingMapper.dtoToEntity(listingRequest, userId, productId, imageUrls, userBasicInfo));
        if(listing == null)
            throw new InternalServerErrorException("Failed to save listing to the database");

        return listing;
    }

    @Override
    public List<ListingResponse> getAllListings() {
        List<Listing> dbListings = listingRepository.getAllListings();
        return dbListings.stream().map(ListingMapper::entityToDto).toList();
    }

    @Override
    public ListingResponse getListingById(String listingId) {
        Listing listing = listingRepository.getListingById(listingId);
        return ListingMapper.entityToDto(listing);
    }
}
