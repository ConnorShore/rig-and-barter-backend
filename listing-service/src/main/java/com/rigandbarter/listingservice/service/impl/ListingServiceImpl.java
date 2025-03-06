package com.rigandbarter.listingservice.service.impl;

import com.rigandbarter.core.models.UserResponse;
import com.rigandbarter.listingservice.client.PaymentServiceClient;
import com.rigandbarter.listingservice.client.TransactionServiceClient;
import com.rigandbarter.listingservice.client.UserServiceClient;
import com.rigandbarter.listingservice.dto.ListingRequest;
import com.rigandbarter.core.models.ListingResponse;
import com.rigandbarter.listingservice.dto.StripeProductRequest;
import com.rigandbarter.listingservice.model.Listing;
import com.rigandbarter.listingservice.repository.document.IListingRepository;
import com.rigandbarter.listingservice.repository.object.IObjectRepository;
import com.rigandbarter.listingservice.repository.mapper.ListingMapper;
import com.rigandbarter.listingservice.service.IListingService;
import com.rigandbarter.core.models.StripeProductCreationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ListingServiceImpl implements IListingService {

    private final IListingRepository listingRepository;
    private final IObjectRepository objectRepository;

    private final UserServiceClient userServiceClient;
    private final PaymentServiceClient paymentServiceClient;
    private final TransactionServiceClient transactionServiceClient;

    @Override
    public ListingResponse createListing(ListingRequest listingRequest, List<MultipartFile> images, Jwt principal) {
        log.info("Creating listing for user: " + principal.getSubject());

        String userId = principal.getSubject();

        UserResponse userInfo = userServiceClient.getUser(userId, "Bearer " + principal.getTokenValue());
        if(userInfo == null)
            throw new NotFoundException("Cannot get info for user. User doesn't exist in db");

        // Create a product in stripe for the listing
        StripeProductRequest stripeProductRequest = StripeProductRequest.builder()
                .userId(userId)
                .name(listingRequest.getTitle())
                .description(listingRequest.getDescription())
                .productPrice(listingRequest.getPrice())
                .build();

        StripeProductCreationResponse product = paymentServiceClient.getPaymentProduct(stripeProductRequest, "Bearer " + principal.getTokenValue());
        if (product.getStripeProductId() == null) {
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
            imageUrls.add(objectRepository.uploadFile(key, image));
        }

        // Save the listing data to the document db
        Listing listing = listingRepository.saveListing(
                ListingMapper.dtoToEntity(listingRequest, userId, product.getStripeProductId(), product.getStripePriceId(), imageUrls,
                        userInfo.getBasicInfo(),
                        userInfo.getStripeInfo().isVerified()
                )
        );
        if(listing == null)
            throw new InternalServerErrorException("Failed to save listing to the database");

        log.info("Successfully saved listing to the database");
        return ListingMapper.entityToDto(listing);
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

    @Override
    public void updateListingPrice(String listingId, double price, String authToken) {
        Listing listing = listingRepository.getListingById(listingId);
        if(listing == null)
            throw new NotFoundException("Listing with id: " + listingId + " not found");

        listing.setPrice(price);
        listingRepository.saveListing(listing);

        paymentServiceClient.updateProductPrice(listing.getStripeProductId(), price, "Bearer " + authToken);
    }

    @Override
    public void setVerificationForListings(String userId, boolean verified) {
        List<Listing> userListings = listingRepository.getAllListingsForUser(userId);
        for(Listing listing : userListings)
            listing.setUserVerified(verified);

        listingRepository.saveListings(userListings);
    }

    @Override
    public void deleteListingById(String listingId, boolean deleteTransaction, String authToken) {
        try {
            // TODO: See if can create a db transaction and if the web request fails, rollback the db transaction

            // Delete images of the listing
            Listing listing = listingRepository.getListingById(listingId);
            List<String> images = listing.getImageUrls().stream()
                    .map(url -> url.substring(url.lastIndexOf("/") + 1))
                    .toList();

            for(String image : images)
                objectRepository.deleteFile(image);

            // Delete the listing
            listingRepository.deleteListingById(listingId);

            // Delete the transaction if needed
            if(deleteTransaction)
                transactionServiceClient.deleteListingTransaction(listingId, "Bearer " + authToken);

        } catch (Exception e) {
            throw new NotFoundException("Failed to delete listing with id: " + listingId + " and associated transactions");
        }
    }
}
