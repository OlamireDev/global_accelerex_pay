package com.olamiredev.accelepay.data;

import com.olamiredev.accelepay.enums.RecognisedBank;
import com.olamiredev.accelepay.model.BankAccount;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetail {

    private String accountNumber;

    private String accountName;

    private RecognisedBank bankName;

    public static AccountDetail fromBankAccount(BankAccount bankAccount){
        return AccountDetail.builder()
                .accountName(bankAccount.getAccountName())
                .accountNumber(bankAccount.getAccountNumber())
                .bankName(bankAccount.getBankName())
                .build();
    }

}
