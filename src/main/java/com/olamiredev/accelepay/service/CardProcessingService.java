package com.olamiredev.accelepay.service;

import com.olamiredev.accelepay.data.AccountDetail;
import com.olamiredev.accelepay.enums.RecognisedBank;
import com.olamiredev.accelepay.payload.request.CardPaymentType;
import com.olamiredev.accelepay.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardProcessingService {

    public Pair<String, AccountDetail> getCardAccountDetails(CardPaymentType cardPaymentType) {
        int randomNumber = (int) (Math.random() * 10);
        if(randomNumber % 2 != 0) { // this is just to simulate a possible API call with a chance of failure
            return Pair.of("Error processing card details", null);
        }
        return Pair.of(null, getRandomAccountDetail(cardPaymentType.getCardHolderName()));
    }

    private AccountDetail getRandomAccountDetail(String cardHolderName) {
        return AccountDetail.builder().accountName(cardHolderName).accountNumber(getRandomBankAccountNumber(10)).bankName(RecognisedBank.getRandomBank()).build();
    }

    private String getRandomBankAccountNumber(int length) {
        long randomNumber = (long) (Math.random() * 99999999999L);
        String randomNumberString = String.valueOf(randomNumber);
        if(randomNumberString.length() < length) {
            return randomNumberString + getRandomBankAccountNumber(length - randomNumberString.length());
        }else{
            return randomNumberString;
        }
    }
}
