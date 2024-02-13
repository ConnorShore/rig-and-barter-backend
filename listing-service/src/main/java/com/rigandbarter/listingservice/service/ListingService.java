package com.rigandbarter.listingservice.service;

import com.rigandbarter.listingservice.dto.ListingRequest;
import com.rigandbarter.listingservice.model.ComponentCategory;
import com.rigandbarter.listingservice.model.Listing;
import com.rigandbarter.listingservice.repository.document.IListingRepository;
import com.rigandbarter.listingservice.repository.file.IFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final IListingRepository listingRepository;
    private final IFileRepository fileRepository;

    /**
     * Creates a new listing in the database and uploads images to blob storage
     * @param listingRequest The listing metadata to save to document db
     * @param image The listing image to save to blob storage
     */
    public void createListing(ListingRequest listingRequest, MultipartFile image) {
        // Prepare a key for file service
        var fileExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        var key = UUID.randomUUID() + "." + fileExtension;

        // Save the listing image to file service
        String imageUrl = fileRepository.uploadFile(key, image);

        // Save the listing data to the document db
        var listing = Listing.builder()
                .id(UUID.randomUUID().toString())
                .userId("DevTest")
                .title(listingRequest.getTitle())
                .description(listingRequest.getDescription())
                .creationDate(LocalDateTime.now())
                .componentCategory(listingRequest.getComponentCategory())
                .imageId(imageUrl)
                .build();

        listingRepository.saveListing(listing);
    }
}
