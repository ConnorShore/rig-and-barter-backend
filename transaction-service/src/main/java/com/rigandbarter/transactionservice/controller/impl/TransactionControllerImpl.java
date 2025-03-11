package com.rigandbarter.transactionservice.controller.impl;

import com.rigandbarter.transactionservice.controller.ITransactionController;
import com.rigandbarter.transactionservice.dto.CompleteTransactionRequest;
import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.core.models.TransactionResponse;
import com.rigandbarter.transactionservice.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TransactionControllerImpl implements ITransactionController {

    private final ITransactionService transactionService;

    @Override
    public String createTransaction(Jwt principal, TransactionRequest transactionRequest) {
        log.info("Create transaction requested for user: " + principal.getSubject() + "; with buyerId: " + transactionRequest.getBuyerId());
        return transactionService.createTransaction(transactionRequest).getUniqueId();
    }

    @Override
    public List<TransactionResponse> getAllTransactions(Jwt principal) {
        return transactionService.getAllTransactionsForUser(principal.getSubject());
    }

    @Override
    public TransactionResponse getTransaction(String transactionId, Jwt principal) {
        return transactionService.getTransactionForUser(transactionId, principal.getSubject());
    }

    @Override
    public List<TransactionResponse> getActiveTransactionsForListing(String listingId) {
        return transactionService.getActiveTransactionsForListing(listingId);
    }

    @Override
    public void deleteTransaction(String id) {
        this.transactionService.deleteTransaction(id);
    }

    @Override
    public TransactionResponse acceptTransaction(String transactionId, Jwt principal) {
        return this.transactionService.acceptTransaction(transactionId, principal);
    }

    @Override
    public TransactionResponse completeTransaction(String transactionId, CompleteTransactionRequest completeTransactionRequest, Jwt principal) {
        return this.transactionService.completeTransaction(
                transactionId,
                completeTransactionRequest.getPaymentMethodId(),
                completeTransactionRequest.isManualTransaction(),
                principal
        );
    }

    @Override
    public TransactionResponse cancelTransaction(String transactionId, Jwt principal) {
        return this.transactionService.cancelTransaction(transactionId, principal.getSubject());
    }

    @Override
    public String healthCheck() {
        return "Transaction service is running...";
    }
}
