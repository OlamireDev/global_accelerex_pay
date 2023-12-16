package com.olamiredev.accelepay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olamiredev.accelepay.data.AccountDetail;
import com.olamiredev.accelepay.data.error.APErrorType;
import com.olamiredev.accelepay.enums.APAccountType;
import com.olamiredev.accelepay.enums.PaymentStatus;
import com.olamiredev.accelepay.enums.PaymentType;
import com.olamiredev.accelepay.enums.UserType;
import com.olamiredev.accelepay.exception.APException;
import com.olamiredev.accelepay.model.BankAccount;
import com.olamiredev.accelepay.model.MerchantTransaction;
import com.olamiredev.accelepay.payload.request.AccelePayPaymentRequest;
import com.olamiredev.accelepay.payload.request.CardPaymentType;
import com.olamiredev.accelepay.payload.request.MerchantPaymentRequest;
import com.olamiredev.accelepay.payload.response.MerchantCardPaymentResponse;
import com.olamiredev.accelepay.payload.response.PaymentResponse;
import com.olamiredev.accelepay.repository.BankAccountRepository;
import com.olamiredev.accelepay.repository.MerchantTransactionRepository;
import com.olamiredev.accelepay.repository.UsersRepository;
import com.olamiredev.accelepay.util.EncryptDecrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Service
public class PaymentProcessingService {

    @Autowired
    UsersRepository userRepo;

    @Autowired
    BankAccountRepository bankAccountRepo;

    @Autowired
    CardProcessingService cardProcessingService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    EncryptDecrypt encryptDecrypt;

    @Autowired
    MerchantTransactionRepository merchantTransactionRepo;

    ObjectMapper objectMapper = new ObjectMapper();

    public PaymentResponse processPayment(AccelePayPaymentRequest accelePayPaymentRequest) throws JsonProcessingException, APException {
        if(accelePayPaymentRequest instanceof MerchantPaymentRequest merchantPaymentRequest) {
            return processMerchantPayment(merchantPaymentRequest);
        }
        throw new APException(APErrorType.BAD_REQUEST, "Invalid payment request", this.getClass().getName());
    }

    /**
     * Process payment for a merchant. This method assumes that the destination for the payment is the merchant's primary account
     * @param paymentRequest
     * @return
     */
    private PaymentResponse processMerchantPayment(MerchantPaymentRequest paymentRequest) throws JsonProcessingException, APException {
        var merchantOpt = userRepo.findByApiKeyAndUserType(encryptDecrypt.encrypt(paymentRequest.getApiKey()), UserType.MERCHANT);
        if(merchantOpt.isEmpty()) {
            throw new APException(APErrorType.UNAUTHORIZED, "Merchant Key provided is not valid", this.getClass().getName());
        }
        var merchant = merchantOpt.get();
        Optional<BankAccount> bankAccountOpt;
        if(paymentRequest.getAlternateAccountId() != null){
            bankAccountOpt = bankAccountRepo.findByUserAndId(merchant, paymentRequest.getAlternateAccountId());
        }else {
            bankAccountOpt = bankAccountRepo.findByUserAndAccountType(merchant, APAccountType.PRIMARY);
        }
        if(bankAccountOpt.isEmpty()) {
            throw new APException(APErrorType.ACCOUNT_PROCESSING_ERROR,
                    "Merchant does not have a primary bank account or account id provided is not valid", this.getClass().getName());
        }
        var bankAccount = bankAccountOpt.get();
        if(paymentRequest.getPaymentType() instanceof CardPaymentType cardPaymentType) {
            var cardProcessingResponse = cardProcessingService.getCardAccountDetails(cardPaymentType);
            if(cardProcessingResponse.hasFirst()) {
                throw new APException(APErrorType.ACCOUNT_PROCESSING_ERROR,
                        cardProcessingResponse.first(), this.getClass().getName());
            }
            var accountDetail = cardProcessingResponse.second();
            var transactionServiceResponse = transactionService.handleAccountTransaction(AccountDetail.fromBankAccount(bankAccount), accountDetail, paymentRequest.getAmountToPay(), paymentRequest.getPaymentDescription());
            var dateTime = LocalDateTime.now();
            var encryptedCardDetails = objectMapper.writeValueAsString(cardPaymentType);
            encryptedCardDetails = encryptDecrypt.encrypt(encryptedCardDetails);
            var referenceId = ((int) (Math.random() * 9999)) + "-"+ Arrays.toString(merchant.getFullName().getBytes()).replaceAll(",","")+"-"+dateTime.getYear()+dateTime.getMonth()+dateTime.getDayOfMonth()+dateTime.getHour()+dateTime.getMinute();
            if(transactionServiceResponse.hasFirst()){
                var merchantTransaction = MerchantTransaction.builder().transactionReferenceNumber(referenceId)
                        .paymentAmount(paymentRequest.getAmountToPay()).user(merchant).paymentDescription(paymentRequest.getPaymentDescription())
                        .paymentType(PaymentType.CARD).paymentTypeDetails(encryptedCardDetails)
                        .paymentStatus(PaymentStatus.FAILED).paymentError(transactionServiceResponse.first().name()).build();
                merchantTransactionRepo.save(merchantTransaction);
                return new MerchantCardPaymentResponse(transactionServiceResponse.first(),referenceId);
            }
            var merchantTransaction = MerchantTransaction.builder().transactionReferenceNumber(referenceId)
                    .paymentAmount(paymentRequest.getAmountToPay()).user(merchant).paymentDescription(paymentRequest.getPaymentDescription())
                    .paymentType(PaymentType.CARD).paymentTypeDetails(encryptedCardDetails).paymentStatus(PaymentStatus.SUCCESSFUL).build();
            merchantTransactionRepo.save(merchantTransaction);
            return new MerchantCardPaymentResponse(referenceId, paymentRequest.getAmountToPay());
        }
        throw new APException(APErrorType.BAD_REQUEST, "Invalid payment request", this.getClass().getName());
    }

    private PaymentResponse processCustomerPayment() {
        return null;
    }

}
