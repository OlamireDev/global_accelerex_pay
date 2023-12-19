package com.olamiredev.accelepay.repository;

import com.olamiredev.accelepay.model.PersonalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalTransactionRepository extends JpaRepository<PersonalTransaction, Long> {
}
