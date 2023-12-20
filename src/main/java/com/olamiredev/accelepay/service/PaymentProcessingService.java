package com.olamiredev.accelepay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olamiredev.accelepay.data.AccountDetail;
import com.olamiredev.accelepay.data.error.APErrorType;
import com.olamiredev.accelepay.enums.APAccountType;
import com.olamiredev.accelepay.enums.PaymentStatus;
import com.olamiredev.accelepay.enums.TransactionPaymentType;
import com.olamiredev.accelepay.enums.UserType;
import com.olamiredev.accelepay.exception.APException;
import com.olamiredev.accelepay.model.BankAccount;
import com.olamiredev.accelepay.model.MerchantTransaction;
import com.olamiredev.accelepay.model.PersonalTransaction;
import com.olamiredev.accelepay.payload.request.payment.get.GetPaymentsRequestDTO;
import com.olamiredev.accelepay.payload.request.payment.make.AccelePayPaymentRequest;
import com.olamiredev.accelepay.payload.request.payment.make.CardPaymentType;
import com.olamiredev.accelepay.payload.request.payment.make.MerchantPaymentRequest;
import com.olamiredev.accelepay.payload.request.payment.make.PersonalPaymentRequest;
import com.olamiredev.accelepay.payload.response.payment.get.*;
import com.olamiredev.accelepay.payload.response.payment.make.DefaultPaymentResponse;
import com.olamiredev.accelepay.payload.response.payment.make.PaymentResponse;
import com.olamiredev.accelepay.repository.BankAccountRepository;
import com.olamiredev.accelepay.repository.MerchantTransactionRepository;
import com.olamiredev.accelepay.repository.PersonalTransactionRepository;
import com.olamiredev.accelepay.repository.UsersRepository;
import com.olamiredev.accelepay.util.EncryptDecrypt;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
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

    @Autowired
    PersonalTransactionRepository personalTransactionRepo;

    ObjectMapper objectMapper = new ObjectMapper();

    public PaymentResponse processPayment(AccelePayPaymentRequest accelePayPaymentRequest) throws JsonProcessingException, APException {
        if(accelePayPaymentRequest instanceof MerchantPaymentRequest merchantPaymentRequest) {
            return processMerchantPayment(merchantPaymentRequest);
        } else if (accelePayPaymentRequest instanceof PersonalPaymentRequest personalPaymentRequest) {
            return processCustomerPayment(personalPaymentRequest);
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
            var encryptedCardDetails = objectMapper.writeValueAsString(cardPaymentType);
            encryptedCardDetails = encryptDecrypt.encrypt(encryptedCardDetails);
            if(transactionServiceResponse.hasFirst()){
                var merchantTransaction = MerchantTransaction.builder().transactionReferenceNumber(generateReferenceId(merchant.getFullName()))
                        .paymentAmount(paymentRequest.getAmountToPay()).user(merchant).paymentDescription(paymentRequest.getPaymentDescription())
                        .transactionPaymentType(TransactionPaymentType.CARD).paymentTypeDetails(encryptedCardDetails)
                        .paymentStatus(PaymentStatus.FAILED).paymentError(transactionServiceResponse.first().name()).build();
                merchantTransactionRepo.save(merchantTransaction);
                return new DefaultPaymentResponse(transactionServiceResponse.first(), merchantTransaction.getTransactionReferenceNumber(), PaymentStatus.FAILED, paymentRequest.getAmountToPay());
            }
            var merchantTransaction = MerchantTransaction.builder().transactionReferenceNumber(generateReferenceId(merchant.getFullName()))
                    .paymentAmount(paymentRequest.getAmountToPay()).user(merchant).paymentDescription(paymentRequest.getPaymentDescription())
                    .transactionPaymentType(TransactionPaymentType.CARD).paymentTypeDetails(encryptedCardDetails).paymentStatus(PaymentStatus.SUCCESSFUL).build();
            merchantTransactionRepo.save(merchantTransaction);
            return new DefaultPaymentResponse(null, merchantTransaction.getTransactionReferenceNumber(), PaymentStatus.SUCCESSFUL, paymentRequest.getAmountToPay());
        }
        throw new APException(APErrorType.BAD_REQUEST, "Invalid payment request", this.getClass().getName());
    }

    private PaymentResponse processCustomerPayment(PersonalPaymentRequest paymentRequest) throws APException, JsonProcessingException {
        var customerOpt = userRepo.findByApiKeyAndUserType(encryptDecrypt.encrypt(paymentRequest.getApiKey()), UserType.PERSONAL);
        if(customerOpt.isEmpty()) {
            throw new APException(APErrorType.UNAUTHORIZED, "Customer Key provided is not valid", this.getClass().getName());
        }
        var customer = customerOpt.get();
        if(paymentRequest.getPaymentType() instanceof CardPaymentType cardPaymentType) {
            var cardProcessingResponse = cardProcessingService.getCardAccountDetails(cardPaymentType);
            if(cardProcessingResponse.hasFirst()) {
                throw new APException(APErrorType.ACCOUNT_PROCESSING_ERROR,
                        cardProcessingResponse.first(), this.getClass().getName());
            }
            var sourceAccountDetail = cardProcessingResponse.second();
            var destinationAccountDetail = AccountDetail.builder().accountName(paymentRequest.getDestinationAccountName())
                    .accountNumber(paymentRequest.getDestinationAccountNumber()).bankName(paymentRequest.getDestinationBank()).build();
            var transactionServiceResponse = transactionService.handleAccountTransaction(destinationAccountDetail, sourceAccountDetail, paymentRequest.getAmountToPay(), paymentRequest.getPaymentDescription());
            var encryptedCardDetails = objectMapper.writeValueAsString(cardPaymentType);
            encryptedCardDetails = encryptDecrypt.encrypt(encryptedCardDetails);
            if(transactionServiceResponse.hasFirst()){
                var personalTransaction = PersonalTransaction.builder().transactionReferenceNumber(generateReferenceId(customer.getFullName()))
                        .paymentAmount(paymentRequest.getAmountToPay()).user(customer).paymentDescription(paymentRequest.getPaymentDescription())
                        .transactionPaymentType(TransactionPaymentType.CARD).paymentTypeDetails(encryptedCardDetails)
                        .paymentStatus(PaymentStatus.FAILED).paymentError(transactionServiceResponse.first().name())
                        .destinationAccountNumber(paymentRequest.getDestinationAccountNumber()).destinationAccountName(paymentRequest.getDestinationAccountName())
                        .destinationBank(paymentRequest.getDestinationBank()).requestPlatform(paymentRequest.getRequestPlatform()).build();
                personalTransactionRepo.save(personalTransaction);
                return new DefaultPaymentResponse(transactionServiceResponse.first(),personalTransaction.getTransactionReferenceNumber(), PaymentStatus.FAILED, paymentRequest.getAmountToPay());
            }
            var personalTransaction = PersonalTransaction.builder().transactionReferenceNumber(generateReferenceId(customer.getFullName()))
                    .paymentAmount(paymentRequest.getAmountToPay()).user(customer).paymentDescription(paymentRequest.getPaymentDescription())
                    .transactionPaymentType(TransactionPaymentType.CARD).paymentTypeDetails(encryptedCardDetails).paymentStatus(PaymentStatus.SUCCESSFUL)
                    .destinationAccountNumber(paymentRequest.getDestinationAccountNumber()).destinationAccountName(paymentRequest.getDestinationAccountName())
                    .destinationBank(paymentRequest.getDestinationBank()).requestPlatform(paymentRequest.getRequestPlatform()).build();
            personalTransactionRepo.save(personalTransaction);
            return new DefaultPaymentResponse(null, personalTransaction.getTransactionReferenceNumber(), PaymentStatus.SUCCESSFUL,paymentRequest.getAmountToPay());
        }
        throw new APException(APErrorType.BAD_REQUEST, "Invalid payment request", this.getClass().getName());
    }

    public GetPaymentsResponse getPayments(GetPaymentsRequestDTO getPaymentsRequest) throws APException{
        var userOpt = userRepo.findByApiKey(encryptDecrypt.encrypt(getPaymentsRequest.getApiKey()));
        if(userOpt.isEmpty()){
            throw new APException(APErrorType.NOT_FOUND, "User with the provided API key not found", this.getClass().getName());
        }
        var user = userOpt.get();
        var pageNumber = getPaymentsRequest.getPageNumber();
        var pageSize = getPaymentsRequest.getPageSize();
        if(pageNumber < 0) pageNumber = 0;
        if(pageSize < 1) pageSize = 1;
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        if(user.getUserType().equals(UserType.MERCHANT)){
            var merchantTransactionPageResponse = merchantTransactionRepo.findAllByUser(user, pageRequest);
            var merchantTransactions = merchantTransactionPageResponse.getContent();
            var merchantTransactionDTOs = merchantTransactions.stream().map(s -> new MerchantTransactionDTO(s, encryptDecrypt)).toList();
            return new GetMerchantPaymentsResponse(merchantTransactionDTOs, merchantTransactionPageResponse.getNumber(), merchantTransactionPageResponse.getSize(), merchantTransactionPageResponse.getTotalPages(), merchantTransactionPageResponse.getTotalElements());
        }else if(user.getUserType().equals(UserType.PERSONAL)){
            var personalTransactionPageResponse = personalTransactionRepo.findAllByUser(user, pageRequest);
            var personalTransactions = personalTransactionPageResponse.getContent();
            var personalTransactionDTOs =personalTransactions.stream().map(s -> new PersonalTransactionDTO(s, encryptDecrypt)).toList();
            return new GetPersonalPaymentsResponse(personalTransactionDTOs, personalTransactionPageResponse.getNumber(), personalTransactionPageResponse.getSize(), personalTransactionPageResponse.getTotalPages(), personalTransactionPageResponse.getTotalElements());
        }
        var error = String.format("User with API key %s is neither a merchant or personal", getPaymentsRequest.getApiKey());
        log.warn(error);
        throw new APException(APErrorType.APPLICATION_EXCEPTION, error, this.getClass().getName());
    }

    private String generateReferenceId(String fullName) {
        var dateTime = LocalDateTime.now();
        var byteArray = fullName.getBytes();
        long sum =0;
        for (byte b : byteArray) {
            sum += b;
        }
        return ((int) (Math.random() * 9999)) + "-"+ sum +"-"+dateTime.getYear()+dateTime.getMonth().ordinal()+dateTime.getDayOfMonth()+dateTime.getHour()+dateTime.getMinute();
    }

}
