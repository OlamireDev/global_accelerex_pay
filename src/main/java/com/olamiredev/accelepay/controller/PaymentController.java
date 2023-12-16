package com.olamiredev.accelepay.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.olamiredev.accelepay.exception.APException;
import com.olamiredev.accelepay.payload.request.AccelePayPaymentRequest;
import com.olamiredev.accelepay.payload.response.PaymentResponse;
import com.olamiredev.accelepay.service.PaymentProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/payment")
@RestController
public class PaymentController {

    @Autowired
    PaymentProcessingService paymentProcessingService;

    @PostMapping("/makePayment")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody AccelePayPaymentRequest accelePayPaymentRequest) throws JsonProcessingException, APException {
        return ResponseEntity.ok(paymentProcessingService.processPayment(accelePayPaymentRequest));
    }

}
