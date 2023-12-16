package com.olamiredev.accelepay.payload.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.olamiredev.accelepay.deserializer.AccelePayPaymentRequestDeserializer;

@JsonDeserialize(using = AccelePayPaymentRequestDeserializer.class)
public abstract class AccelePayPaymentRequest {
}
