package com.rigandbarter.listingservice.service.impl;

import com.rigandbarter.listingservice.dto.ListingRequest;
import com.rigandbarter.listingservice.dto.ListingResponse;
import com.rigandbarter.listingservice.model.Listing;
import com.rigandbarter.listingservice.repository.document.IListingRepository;
import com.rigandbarter.listingservice.repository.file.IFileRepository;
import com.rigandbarter.listingservice.repository.mapper.ListingMapper;
import com.rigandbarter.listingservice.service.IListingService;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
    public Listing createListing(ListingRequest listingRequest, List<MultipartFile> images, String userId) {
        // Save the listing image to file service
        List<String> imageUrls = new ArrayList<>();
        for(MultipartFile image : images) {
            var fileExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());
            var key = UUID.randomUUID() + "." + fileExtension;
            imageUrls.add(fileRepository.uploadFile(key, image));
        }

        // Save the listing data to the document db
        Listing listing = listingRepository.saveListing(ListingMapper.dtoToEntity(listingRequest, userId, imageUrls));
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
