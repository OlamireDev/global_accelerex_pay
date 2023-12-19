package com.olamiredev.accelepay.model;

import com.olamiredev.accelepay.enums.PaymentStatus;
import com.olamiredev.accelepay.enums.RecognisedBank;
import com.olamiredev.accelepay.enums.SupportedPlatform;
import com.olamiredev.accelepay.enums.TransactionPaymentType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PersonalTransaction extends BaseModel{

    @ManyToOne
    private User user;

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

}
