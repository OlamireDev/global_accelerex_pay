package com.olamiredev.accelepay.util;

import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olamiredev.accelepay.payload.request.payment.make.CardPaymentType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CardHasher {
    
    public static String hashCardNumbers(String cardDetails, EncryptDecrypt encryptDecrypt){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            cardDetails = encryptDecrypt.decrypt(cardDetails);
            cardDetails = cardDetails.substring(1, cardDetails.length() - 1);
            cardDetails = cardDetails.replaceAll("\",\"", "\"<->\"");
            var cardDetailsArray = cardDetails.split("<->");
            System.out.println(Arrays.toString(cardDetailsArray));
            String cardNumber = cardDetailsArray[0].split("\":\"")[1].replaceAll("\"", "");
            cardNumber = cardNumber.substring(0, 6).concat("******").concat(cardNumber.substring(12));
            String cardExpiry = cardDetailsArray[1].split("\":\"")[1].replaceAll("\"", "");
            String cardHoldersName = cardDetailsArray[4].split("\":\"")[1].replaceAll("\"", "");
            var cardPaymentType = new CardPaymentType(cardNumber, cardExpiry, null, null, cardHoldersName);
            return objectMapper.writeValueAsString(cardPaymentType);
        } catch (Exception e) {
            log.warn("Card details provided could not be hashed due to {}", e.getMessage());
            return null;
        } 
    }

}
