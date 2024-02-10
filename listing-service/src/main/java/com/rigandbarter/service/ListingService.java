package com.rigandbarter.service;

import com.rigandbarter.dto.ListingRequest;
import com.rigandbarter.model.ComponentCategory;
import com.rigandbarter.model.Listing;
import com.rigandbarter.repository.IListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final IFileService fileService;
    private final IListingRepository listingRepository;

//    private final WebClient.Builder webClientBuilder;

    /**
     * Creates a new listing in the database and uploads images to blob storage
     * @param listingRequest The listing metadata to save to document db
     * @param image The listing image to save to blob storage
     */
    public void createListing(ListingRequest listingRequest, MultipartFile image) {
        // Save the listing image to file service
        String imageUrl = fileService.uploadFile(image);

        // Save the listing data to the document db
        var listing = Listing.builder()
                .id(UUID.randomUUID().toString())
                .userId("Connor")
                .title(listingRequest.getTitle())
                .description(listingRequest.getDescription())
                .creationDate(LocalDateTime.now())
                .componentCategory(ComponentCategory.CPU)
                .imageId(imageUrl)
                .build();

    }

    public void testBlob(MultipartFile file) {
        fileService.uploadFile(file);
    }

    public void testDocument() {
        var listing = Listing.builder()
                .id("12345")
                .userId("Connor")
                .title("Test item")
                .description("Test descr")
                .creationDate(LocalDateTime.now())
                .componentCategory(ComponentCategory.CPU)
                .imageId("imageID")
                .build();
        listingRepository.saveListing(listing);
    }

    /**
     * Test inter-service communication
     */
//    public String test() {
//
//        String ret = webClientBuilder.build().get()
//                .uri("http://transaction-service/api/transaction/test")
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        return ret;
//    }

}
