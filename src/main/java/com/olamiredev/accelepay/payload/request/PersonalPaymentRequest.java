package com.olamiredev.accelepay.payload.request;

import com.olamiredev.accelepay.enums.RecognisedBank;
import com.olamiredev.accelepay.enums.SupportedPlatform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalPaymentRequest extends AccelePayPaymentRequest {

    private String apiKey;

    private BigDecimal amountToPay;

    private PaymentType paymentType;

    private String paymentDescription;

    private String destinationAccountNumber;

    private RecognisedBank destinationBank;

    private String destinationAccountName;

    private SupportedPlatform requestPlatform;

}
