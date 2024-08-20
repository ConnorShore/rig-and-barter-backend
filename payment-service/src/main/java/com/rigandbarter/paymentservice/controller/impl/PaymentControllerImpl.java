package com.rigandbarter.paymentservice.controller.impl;

import com.rigandbarter.paymentservice.controller.IPaymentController;
import com.rigandbarter.paymentservice.dto.StripeProductRequest;
import com.rigandbarter.paymentservice.service.IPaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentControllerImpl implements IPaymentController {

    private final IPaymentService paymentService;

    @Override
    public String createProduct(StripeProductRequest productRequest) throws StripeException {
        return paymentService.createStripeProduct(productRequest);
    }

    @Override
    public String healthCheck() {
        return "Payment service is running...";
    }
}
