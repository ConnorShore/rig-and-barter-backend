package com.rigandbarter.transactionservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private String listingId;
    private String buyerId;
    private String sellerId;
    private String title;
}
