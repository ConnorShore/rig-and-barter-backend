package com.rigandbarter.paymentservice.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping("api/payment")
public interface IPaymentController {

    @GetMapping("status")
    @ResponseStatus(HttpStatus.OK)
    String healthCheck();
}
