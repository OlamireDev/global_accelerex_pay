package com.olamiredev.accelepay.repository;

import com.olamiredev.accelepay.enums.APAccountType;
import com.olamiredev.accelepay.model.BankAccount;
import com.olamiredev.accelepay.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByUserAndAccountType(User user, APAccountType APAccountType);

    Optional<BankAccount> findByUserAndId(User user, Long id);

}
