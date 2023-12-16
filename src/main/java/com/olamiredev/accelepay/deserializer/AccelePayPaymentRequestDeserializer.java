package com.olamiredev.accelepay.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olamiredev.accelepay.payload.request.AccelePayPaymentRequest;
import com.olamiredev.accelepay.payload.request.MerchantPaymentRequest;
import com.olamiredev.accelepay.payload.request.PaymentType;

import java.io.IOException;
import java.math.BigDecimal;

public class AccelePayPaymentRequestDeserializer extends JsonDeserializer<AccelePayPaymentRequest> {

    @Override
    public AccelePayPaymentRequest deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode node = mapper.readTree(jsonParser);
        System.out.println(node.toString());
        if(node.hasNonNull("apiKey") && node.hasNonNull("amountToPay") && node.hasNonNull("paymentType") && node.hasNonNull("paymentDescription") && node.has("alternateAccountId")) {
            System.out.println("entered");
            String apiKey = node.get("apiKey").asText();
            BigDecimal amountToPay = node.get("amountToPay").decimalValue();
            String paymentDescription = node.get("paymentDescription").asText();
            Long alternateAccountId = null;
            if(node.hasNonNull("alternateAccountId")) {
                alternateAccountId = node.get("alternateAccountId").asLong();
            }
            PaymentType paymentTypeNode = mapper.readValue(node.get("paymentType").toString(), PaymentType.class);
            return new MerchantPaymentRequest(apiKey, amountToPay, paymentTypeNode, paymentDescription, alternateAccountId);
        }
        throw new IOException("Invalid payment request");
    }
}
