package com.rigandbarter.transactionservice.controller.impl;

import com.rigandbarter.transactionservice.controller.ITransactionController;
import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.dto.TransactionResponse;
import com.rigandbarter.transactionservice.service.ITransactionService;
import com.rigandbarter.transactionservice.service.impl.TransactionServiceImpl;
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
    public void acceptTransaction(String transactionId, Jwt principal) {
        this.transactionService.acceptTransaction(transactionId, principal.getSubject());
    }

    @Override
    public void completeTransaction(String transactionId, Jwt principal) {
        this.transactionService.completeTransaction(transactionId, principal.getSubject());
    }
    @Override
    public String healthCheck() {
        return "Transaction service is running...";
    }
}
