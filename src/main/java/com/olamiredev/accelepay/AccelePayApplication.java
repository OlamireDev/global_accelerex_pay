package com.olamiredev.accelepay;

import com.olamiredev.accelepay.enums.APAccountType;
import com.olamiredev.accelepay.enums.RecognisedBank;
import com.olamiredev.accelepay.enums.UserType;
import com.olamiredev.accelepay.model.BankAccount;
import com.olamiredev.accelepay.model.User;
import com.olamiredev.accelepay.repository.BankAccountRepository;
import com.olamiredev.accelepay.repository.UsersRepository;
import com.olamiredev.accelepay.util.EncryptDecrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccelePayApplication implements CommandLineRunner {

    @Autowired
    UsersRepository usersRepo;

    @Autowired
    BankAccountRepository bankAccountRepo;

    @Autowired
    EncryptDecrypt encrypt;

    public static void main(String[] args) {
        SpringApplication.run(AccelePayApplication.class, args);
    }

    @Override
    public void run(String... args) {
        var individual = User.builder().fullName("George Agboghoroma").apiKey(encrypt.encrypt("01234ADEDC88909")).phoneNumber("08052267781").email("olamiretj@gmail.com").userType(UserType.PERSONAL).build();
        individual = usersRepo.save(individual);
        var iBankAccount = BankAccount.builder().user(individual).accountNumber("1234567890").bankName(RecognisedBank.GTB).accountName("Uruntajirinere G Agboghoroma").accountType(APAccountType.PRIMARY).build();
        iBankAccount =bankAccountRepo.save(iBankAccount);
        var merchant = User.builder().fullName("Adora's Skin Care").apiKey(encrypt.encrypt("01234ADEDC8890A")).phoneNumber("08052267789").email("ad_professional@gmail.com").userType(UserType.MERCHANT).build();
        merchant = usersRepo.save(merchant);
        var mBankAccount1 = BankAccount.builder().user(merchant).accountNumber("0088127568").bankName(RecognisedBank.CITIBANK).accountName("Adora's Skin Care").accountType(APAccountType.PRIMARY).build();
        var mBankAccount2 = BankAccount.builder().user(merchant).accountNumber("0088127569").bankName(RecognisedBank.CITIBANK).accountName("Adora Akunyili").accountType(APAccountType.SECONDARY).build();
        mBankAccount1 = bankAccountRepo.save(mBankAccount1);
        mBankAccount2 = bankAccountRepo.save(mBankAccount2);

    }
}
