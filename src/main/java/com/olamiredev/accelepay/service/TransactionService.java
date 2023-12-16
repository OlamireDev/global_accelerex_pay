package com.olamiredev.accelepay.service;

import com.olamiredev.accelepay.data.AccountDetail;
import com.olamiredev.accelepay.data.error.PaymentRequestError;
import com.olamiredev.accelepay.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    public Pair<PaymentRequestError, String> handleAccountTransaction(AccountDetail destinationAccount, AccountDetail sourceAccount, BigDecimal amount, String narration) {
        return Pair.of(null, "Transaction successful");
    }

}
