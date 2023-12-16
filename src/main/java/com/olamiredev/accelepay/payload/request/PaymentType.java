package com.olamiredev.accelepay.payload.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.olamiredev.accelepay.deserializer.PaymentTypeDeserializer;

@JsonDeserialize(using = PaymentTypeDeserializer.class)
public interface PaymentType {

}
