package com.rigandbarter.listingservice.service.impl;

import com.rigandbarter.listingservice.dto.ListingRequest;
import com.rigandbarter.listingservice.dto.ListingResponse;
import com.rigandbarter.listingservice.model.Listing;
import com.rigandbarter.listingservice.repository.document.IListingRepository;
import com.rigandbarter.listingservice.repository.file.IFileRepository;
import com.rigandbarter.listingservice.service.IListingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingServiceImpl implements IListingService {

    private final IListingRepository listingRepository;
    private final IFileRepository fileRepository;

    @Override
    public void createListing(ListingRequest listingRequest, List<MultipartFile> images, String userId) {
        // Save the listing image to file service
        List<String> imageUrls = new ArrayList<>();
        for(MultipartFile image : images) {
            var fileExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());
            var key = UUID.randomUUID() + "." + fileExtension;
            imageUrls.add(fileRepository.uploadFile(key, image));
        }

        // Save the listing data to the document db
        var listing = Listing.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .title(listingRequest.getTitle())
                .description(listingRequest.getDescription())
                .price(listingRequest.getPrice())
                .creationDate(LocalDateTime.now())
                .componentCategory(listingRequest.getComponentCategory())
                .imageUrls(imageUrls)
                .build();

        listingRepository.saveListing(listing);
        log.info("Successfully created listing");
    }

    @Override
    public List<ListingResponse> getAllListings() {
        List<Listing> dbListings = listingRepository.getAllListings();
        return dbListings.stream().map(this::convertListingToListingResponse).toList();
    }

    @Override
    public ListingResponse getListingById(String listingId) {
        Listing listing = listingRepository.getListingById(listingId);
        return convertListingToListingResponse(listing);
    }

    /**
     * Helper to convert Listing to ListingResponse
     * @param listing The db Listing item
     * @return A ListingResponse object
     */
    private ListingResponse convertListingToListingResponse(Listing listing) {
        return ListingResponse.builder()
                .id(listing.getId())
                .userId(listing.getUserId())
                .title(listing.getTitle())
                .description(listing.getDescription())
                .price(listing.getPrice())
                .creationDate(listing.getCreationDate())
                .componentCategory(listing.getComponentCategory())
                .imageUrls(listing.getImageUrls())
                .build();
    }
}