package com.olamiredev.accelepay.repository;

import com.olamiredev.accelepay.model.MerchantTransaction;
import com.olamiredev.accelepay.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantTransactionRepository extends JpaRepository<MerchantTransaction, Long> {

    Page<MerchantTransaction>  findAllByUser(User user, Pageable pageable);

}
