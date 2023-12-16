package com.olamiredev.accelepay.payload.response;

import com.olamiredev.accelepay.data.error.PaymentRequestError;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class MerchantCardPaymentResponse  extends PaymentResponse{

    private PaymentRequestError error;

    private String paymentReference;

    private BigDecimal paymentAmount;


    public MerchantCardPaymentResponse(PaymentRequestError error, String paymentReference) {
        this.error = error;
        this.paymentReference = paymentReference;
    }

    public MerchantCardPaymentResponse(String paymentReference, BigDecimal paymentAmount) {
        this.paymentReference = paymentReference;
        this.paymentAmount = paymentAmount;
    }

}
