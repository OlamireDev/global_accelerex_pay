package com.olamiredev.accelepay.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olamiredev.accelepay.payload.request.CardPaymentType;
import com.olamiredev.accelepay.payload.request.PaymentType;

import java.io.IOException;

public class PaymentTypeDeserializer extends JsonDeserializer<PaymentType> {
    @Override
    public PaymentType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode node = mapper.readTree(jsonParser);
        if(node.hasNonNull("cardNumber") || node.hasNonNull("cardExpiry")) {
            String cardNumber = node.get("cardNumber").asText();
            String cardExpiry = node.get("cardExpiry").asText();
            String cardCcv = node.get("cardCcv").asText();
            String cardPin = node.get("cardPin").asText();
            String cardHolderName = node.get("cardHoldersName").asText();
            return new CardPaymentType(cardNumber, cardExpiry, cardCcv, cardPin, cardHolderName);
        }
        throw new IOException("Invalid payment request");
    }
}
