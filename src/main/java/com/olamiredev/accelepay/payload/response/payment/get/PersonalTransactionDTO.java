package com.olamiredev.accelepay.payload.response.payment.get;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.olamiredev.accelepay.enums.PaymentStatus;
import com.olamiredev.accelepay.enums.RecognisedBank;
import com.olamiredev.accelepay.enums.SupportedPlatform;
import com.olamiredev.accelepay.enums.TransactionPaymentType;
import com.olamiredev.accelepay.model.PersonalTransaction;
import com.olamiredev.accelepay.util.CardHasher;
import com.olamiredev.accelepay.util.EncryptDecrypt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalTransactionDTO{

    private String transactionReferenceNumber;

    private String paymentDescription;

    private BigDecimal paymentAmount;

    private TransactionPaymentType transactionPaymentType;

    private String paymentTypeDetails;

    private PaymentStatus paymentStatus;

    private String paymentError;

    private String destinationAccountNumber;

    private String destinationAccountName;

    private RecognisedBank destinationBank;

    private SupportedPlatform requestPlatform;

    private LocalDateTime transactionDate;


    public PersonalTransactionDTO(PersonalTransaction personalTransaction, EncryptDecrypt encryptDecrypt){
        this.transactionReferenceNumber = personalTransaction.getTransactionReferenceNumber();
        this.paymentDescription = personalTransaction.getPaymentDescription();
        this.paymentAmount = personalTransaction.getPaymentAmount();
        this.transactionPaymentType = personalTransaction.getTransactionPaymentType();
        if(transactionPaymentType.equals(TransactionPaymentType.CARD)){
            this.paymentTypeDetails = CardHasher.hashCardNumbers(personalTransaction.getPaymentTypeDetails(), encryptDecrypt);
        }
        this.paymentStatus = personalTransaction.getPaymentStatus();
        this.paymentError = personalTransaction.getPaymentError();
        this.destinationAccountNumber = personalTransaction.getDestinationAccountNumber();
        this.destinationAccountName = personalTransaction.getDestinationAccountName();
        this.destinationBank = personalTransaction.getDestinationBank();
        this.requestPlatform = personalTransaction.getRequestPlatform();
        this.transactionDate = personalTransaction.getCreatedAt();
    }

}
