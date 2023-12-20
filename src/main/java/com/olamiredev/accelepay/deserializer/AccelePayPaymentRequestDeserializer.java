package com.olamiredev.accelepay.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olamiredev.accelepay.enums.RecognisedBank;
import com.olamiredev.accelepay.enums.SupportedPlatform;
import com.olamiredev.accelepay.payload.request.payment.make.AccelePayPaymentRequest;
import com.olamiredev.accelepay.payload.request.payment.make.MerchantPaymentRequest;
import com.olamiredev.accelepay.payload.request.payment.make.PaymentType;
import com.olamiredev.accelepay.payload.request.payment.make.PersonalPaymentRequest;
import lombok.SneakyThrows;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

public class AccelePayPaymentRequestDeserializer extends JsonDeserializer<AccelePayPaymentRequest> {

    @SneakyThrows
    @Override
    public AccelePayPaymentRequest deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode node = mapper.readTree(jsonParser);
        if(node.hasNonNull("apiKey") && node.hasNonNull("amountToPay") && node.hasNonNull("paymentType") && node.hasNonNull("paymentDescription") && node.has("alternateAccountId")) {
            String apiKey = node.get("apiKey").asText();
            BigDecimal amountToPay = node.get("amountToPay").decimalValue();
            String paymentDescription = node.get("paymentDescription").asText();
            Long alternateAccountId = null;
            if(node.hasNonNull("alternateAccountId")) {
                alternateAccountId = node.get("alternateAccountId").asLong();
            }
            SupportedPlatform requestPlatform = SupportedPlatform.fromString(node.get("requestPlatform").asText());
            PaymentType paymentTypeNode = mapper.readValue(node.get("paymentType").toString(), PaymentType.class);
            return new MerchantPaymentRequest(apiKey, amountToPay, paymentTypeNode, paymentDescription, alternateAccountId, requestPlatform);
        } else if (node.hasNonNull("apiKey") && node.hasNonNull("amountToPay") && node.hasNonNull("paymentType") && node.hasNonNull("paymentDescription") && node.has("destinationAccountNumber")) {
            String apiKey = node.get("apiKey").asText();
            BigDecimal amountToPay = node.get("amountToPay").decimalValue();
            PaymentType paymentType = mapper.readValue(node.get("paymentType").toString(), PaymentType.class);
            String paymentDescription = node.get("paymentDescription").asText();
            String destinationAccountNumber = node.get("destinationAccountNumber").asText();
            Optional<RecognisedBank> destinationBankOpt = RecognisedBank.getBank(node.get("destinationBank").asText());
            if(destinationBankOpt.isEmpty()) {
                throw new IOException("Invalid destination bank");
            }
            String destinationAccountName = node.get("destinationAccountName").asText();
            SupportedPlatform requestPlatform = SupportedPlatform.fromString(node.get("requestPlatform").asText());
            return new PersonalPaymentRequest(apiKey, amountToPay, paymentType, paymentDescription, destinationAccountNumber, destinationBankOpt.get(), destinationAccountName, requestPlatform);
        }
        throw new IOException("Invalid payment request");
    }
}
