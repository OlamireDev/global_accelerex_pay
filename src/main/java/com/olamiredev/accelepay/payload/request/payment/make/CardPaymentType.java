package com.olamiredev.accelepay.payload.request.payment.make;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardPaymentType implements PaymentType{

    private String cardNumber;

    private String cardExpiry;

    private String cardCcv;

    private String cardPin;

    private String cardHolderName;


}
