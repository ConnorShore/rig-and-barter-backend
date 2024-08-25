package com.rigandbarter.transactionservice.controller.impl;

import com.rigandbarter.transactionservice.controller.ITransactionController;
import com.rigandbarter.transactionservice.dto.CompleteTransactionRequest;
import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.dto.TransactionResponse;
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
    public TransactionResponse getTransaciton(String transactionId, Jwt principal) {
        return transactionService.getTransactionForUser(transactionId, principal.getSubject());
    }

    @Override
    public void setTransactionSetupIntentId(String transactionId, String setupIntentId, Jwt principal) {
        transactionService.setTransactionSetupIntentId(transactionId, setupIntentId);
    }

    @Override
    public void acceptTransaction(String transactionId, Jwt principal) {
        this.transactionService.acceptTransaction(transactionId, principal);
    }

    @Override
    public void completeTransaction(String transactionId, CompleteTransactionRequest completeTransactionRequest, Jwt principal) {
        this.transactionService.completeTransaction(transactionId, completeTransactionRequest.getPaymentMethodId(), principal);
    }
    @Override
    public String healthCheck() {
        return "Transaction service is running...";
    }
}
