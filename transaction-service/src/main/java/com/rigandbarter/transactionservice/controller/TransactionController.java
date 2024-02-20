package com.rigandbarter.transactionservice.controller;

import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/transaction")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String createTransaction(@AuthenticationPrincipal Jwt principal,
                                    @RequestBody TransactionRequest transactionRequest) {
        log.info("Create transaction requested for user: " + principal.getId());
        return transactionService.createTransaction(transactionRequest, principal.getId()).getUniqueId();
    }
}
