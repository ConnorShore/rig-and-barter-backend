package com.rigandbarter.transactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTransactionRequest {
    private String transactionId;
    private boolean manualTransaction;
    private String paymentMethodId;
}
