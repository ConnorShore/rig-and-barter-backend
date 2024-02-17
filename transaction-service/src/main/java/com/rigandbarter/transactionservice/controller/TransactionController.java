package com.rigandbarter.transactionservice.controller;

import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String createTransaction(@AuthenticationPrincipal Jwt principal,
                                    @RequestBody TransactionRequest transactionRequest) {
        return transactionService.createTransaction(transactionRequest, principal.getId())
                .getId()
                .toString();
    }
}
