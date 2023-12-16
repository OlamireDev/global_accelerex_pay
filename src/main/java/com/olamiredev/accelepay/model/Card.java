package com.olamiredev.accelepay.model;

import com.olamiredev.accelepay.enums.CardType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table
@RequiredArgsConstructor
@Getter
@Setter
public class Card extends BaseModel{

    @OneToOne
    private User user;

    private String cardNumber;

    private String hashedCardNumber;

    private String expirationDate;

    private String cardPin;

    private String cvc;

    private String cardHolderName;

    private CardType cardType;




}
