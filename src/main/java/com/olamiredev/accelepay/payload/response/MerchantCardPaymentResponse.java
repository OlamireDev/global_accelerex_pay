package com.olamiredev.accelepay.payload.response;

import com.olamiredev.accelepay.data.error.PaymentRequestError;
import com.olamiredev.accelepay.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantCardPaymentResponse  extends PaymentResponse{

    private PaymentRequestError error;

    private String paymentReference;

    private PaymentStatus paymentStatus;

    private BigDecimal paymentAmount;

}
