package com.rigandbarter.transactionservice.controller.impl;

import com.rigandbarter.transactionservice.controller.ITransactionController;
import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.service.impl.TransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TransactionControllerImpl implements ITransactionController {

    private final TransactionServiceImpl transactionService;

    @Override
    public String createTransaction(Jwt principal, TransactionRequest transactionRequest) {
        log.info("Create transaction requested for user: " + principal.getId() + "; with buyerId: " + transactionRequest.getBuyerId());
        return transactionService.createTransaction(transactionRequest).getUniqueId();
    }

    @Override
    public String healthCheck() {
        return "Transaction service is running...";
    }
}
