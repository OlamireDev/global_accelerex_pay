package com.olamiredev.accelepay.model;

import com.olamiredev.accelepay.enums.APAccountType;
import com.olamiredev.accelepay.enums.RecognisedBank;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BankAccount extends BaseModel{

   @ManyToOne
   private User user;

   private String accountNumber;

   private String accountName;

   private RecognisedBank bankName;

   private APAccountType accountType;

}
