package com.olamiredev.accelepay.repository;

import com.olamiredev.accelepay.model.MerchantTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantTransactionRepository extends JpaRepository<MerchantTransaction, Long> {
}
