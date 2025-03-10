package com.rigandbarter.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String uniqueId;
    private String title;
    private String buyerId;
    private String sellerId;
    private String listingId;
    private LocalDateTime creationDate;
    private LocalDateTime completionDate;
    private boolean buyerAccepted;
    private boolean sellerAccepted;
    private boolean buyerCompleted;
    private boolean sellerCompleted;
    private TransactionState state;
}
