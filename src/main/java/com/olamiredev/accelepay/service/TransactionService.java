package com.olamiredev.accelepay.service;

import com.olamiredev.accelepay.data.AccountDetail;
import com.olamiredev.accelepay.data.error.PaymentRequestError;
import com.olamiredev.accelepay.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    public Pair<PaymentRequestError, String> handleAccountTransaction(AccountDetail destinationAccount, AccountDetail sourceAccount, BigDecimal amount, String narration) {
        int random = (int) (Math.random() * 100);
        if (random % 5 == 0) {
            return Pair.of(PaymentRequestError.TRANSACTION_FAILED_DUE_INVALID_ACCOUNT_DETAILS, "Account details are invalid");
        } else if (random % 3 ==0) {
            return Pair.of(PaymentRequestError.TRANSACTION_FAILED_DUE_TO_INSUFFICIENT_FUNDS, "Insufficient balance");
        }
        return Pair.of(null, "Transaction successful");
    }

}
