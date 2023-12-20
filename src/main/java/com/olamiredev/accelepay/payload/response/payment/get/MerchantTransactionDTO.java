package com.olamiredev.accelepay.payload.response.payment.get;

import com.olamiredev.accelepay.enums.PaymentStatus;
import com.olamiredev.accelepay.enums.SupportedPlatform;
import com.olamiredev.accelepay.enums.TransactionPaymentType;
import com.olamiredev.accelepay.model.MerchantTransaction;
import com.olamiredev.accelepay.util.CardHasher;
import com.olamiredev.accelepay.util.EncryptDecrypt;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class MerchantTransactionDTO {

    private String transactionReferenceNumber;

    private String paymentDescription;

    private BigDecimal paymentAmount;

    private TransactionPaymentType transactionPaymentType;

    private String paymentTypeDetails;

    private PaymentStatus paymentStatus;

    private String paymentError;

    private SupportedPlatform requestPlatform;

    private LocalDateTime transactionDate;


    public MerchantTransactionDTO(MerchantTransaction merchantTransaction, EncryptDecrypt encryptDecrypt){
        this.transactionReferenceNumber = merchantTransaction.getTransactionReferenceNumber();
        this.paymentDescription = merchantTransaction.getPaymentDescription();
        this.paymentAmount = merchantTransaction.getPaymentAmount();
        this.transactionPaymentType = merchantTransaction.getTransactionPaymentType();
        if(transactionPaymentType.equals(TransactionPaymentType.CARD)){
            this.paymentTypeDetails = CardHasher.hashCardNumbers(merchantTransaction.getPaymentTypeDetails(), encryptDecrypt);
        }
        this.paymentStatus = merchantTransaction.getPaymentStatus();
        this.paymentError = merchantTransaction.getPaymentError();
        this.requestPlatform = merchantTransaction.getRequestPlatform();
        this.transactionDate = merchantTransaction.getCreatedAt();
    }


}
