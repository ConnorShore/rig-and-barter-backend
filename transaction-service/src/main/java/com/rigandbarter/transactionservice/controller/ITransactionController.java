package com.rigandbarter.transactionservice.controller;

import com.rigandbarter.transactionservice.dto.TransactionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/transaction")
public interface ITransactionController {

    /**
     * Endpoint to create a transaction
     * @param principal The auth principal (user)
     * @param transactionRequest The details of the transaction to create
     * @return The ID of the created transaction
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    String createTransaction(@AuthenticationPrincipal Jwt principal,
                                    @RequestBody TransactionRequest transactionRequest);

    /**
     * The status endpoint to see if the service is running
     */
    @GetMapping("status")
    String healthCheck();
}
