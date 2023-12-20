package com.olamiredev.accelepay.payload.request.payment.make;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.olamiredev.accelepay.deserializer.AccelePayPaymentRequestDeserializer;

@JsonDeserialize(using = AccelePayPaymentRequestDeserializer.class)
public abstract class AccelePayPaymentRequest {
}
