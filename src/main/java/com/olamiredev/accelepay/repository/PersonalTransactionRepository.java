package com.olamiredev.accelepay.repository;

import com.olamiredev.accelepay.model.PersonalTransaction;
import com.olamiredev.accelepay.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalTransactionRepository extends JpaRepository<PersonalTransaction, Long> {

    Page<PersonalTransaction> findAllByUser(User user, Pageable pageable);

}
