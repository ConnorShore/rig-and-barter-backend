package com.rigandbarter.eventlibrary.events;

import com.rigandbarter.eventlibrary.model.RBEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionInProgressEvent extends RBEvent {
    private String transactionId;
    private String buyerId;
    private String sellerId;
    private String listingId;
    private String authToken;
}
