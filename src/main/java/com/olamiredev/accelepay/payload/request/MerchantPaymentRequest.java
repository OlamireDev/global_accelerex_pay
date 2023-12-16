package com.olamiredev.accelepay.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantPaymentRequest extends AccelePayPaymentRequest {

    private String apiKey;

    private BigDecimal amountToPay;

    private PaymentType paymentType;

    private String paymentDescription;

    private Long alternateAccountId;

}
