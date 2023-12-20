package com.olamiredev.accelepay.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.olamiredev.accelepay.exception.APException;
import com.olamiredev.accelepay.payload.request.payment.get.GetPaymentsRequestDTO;
import com.olamiredev.accelepay.payload.request.payment.make.AccelePayPaymentRequest;
import com.olamiredev.accelepay.payload.response.payment.get.GetPaymentsResponse;
import com.olamiredev.accelepay.payload.response.payment.make.PaymentResponse;
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

    @PostMapping("/getPayments")
    public ResponseEntity<GetPaymentsResponse> postMethodName(@RequestBody GetPaymentsRequestDTO paymentsRequestDTO) throws APException {
        return ResponseEntity.ok(paymentProcessingService.getPayments(paymentsRequestDTO));
    }
    

}
