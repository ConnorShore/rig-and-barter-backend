package com.rigandbarter.paymentservice.controller.impl;

import com.rigandbarter.paymentservice.controller.IPaymentController;
import com.rigandbarter.paymentservice.service.IPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentControllerImpl implements IPaymentController {

    private final IPaymentService paymentService;

    @Override
    public String healthCheck() {
        return "Payment service is running...";
    }
}
