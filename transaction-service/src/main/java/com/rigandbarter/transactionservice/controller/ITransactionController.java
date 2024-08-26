package com.rigandbarter.transactionservice.controller;

import com.rigandbarter.transactionservice.dto.CompleteTransactionRequest;
import com.rigandbarter.transactionservice.dto.TransactionRequest;
import com.rigandbarter.transactionservice.dto.TransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * Gets all active transactions for the user
     * @param principal The auth principal (user)
     * @return All active transactions for the user
     */
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    List<TransactionResponse> getAllTransactions(@AuthenticationPrincipal Jwt principal);

    /**
     * Gets the specified transactions for the user
     * @param principal The auth principal (user)
     * @return Specified transactions for the user
     */
    @GetMapping("{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    TransactionResponse getTransaction(@PathVariable String transactionId, @AuthenticationPrincipal Jwt principal);

    /**
     * Deletes the specified transactions for the user
     * @param id The id of the transaciton to delete (may be listingId as well)
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteTransaction(@PathVariable String id);

    /**
     * Accepts the transaction for the specified user
     * @param transactionId The id of the transaction to accept
     * @param principal The user auth principal
     */
    @PutMapping("{transactionId}/accept")
    @ResponseStatus(HttpStatus.OK)
    TransactionResponse acceptTransaction(@PathVariable String transactionId, @AuthenticationPrincipal Jwt principal);

    /**
     * Completes the specified transaction
     * @param transactionId The id of the transaction to complete
     * @param principal The user auth principal
     */
    @PutMapping("{transactionId}/complete")
    @ResponseStatus(HttpStatus.OK)
    TransactionResponse completeTransaction(@PathVariable String transactionId,
                             @RequestBody CompleteTransactionRequest request,
                             @AuthenticationPrincipal Jwt principal);
    /**
     * Cancels the specified transaction
     * @param transactionId The id of the transaction to cancel
     * @param principal The user auth principal
     */
    @PutMapping("{transactionId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    TransactionResponse cancelTransaction(@PathVariable String transactionId,
                                            @AuthenticationPrincipal Jwt principal);

    /**
     * The status endpoint to see if the service is running
     */
    @GetMapping("status")
    String healthCheck();
}
