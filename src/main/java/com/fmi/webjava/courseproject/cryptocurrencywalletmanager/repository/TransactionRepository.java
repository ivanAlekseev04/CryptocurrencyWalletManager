package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.repository;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.transaction.Transaction;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.transaction.TransactionId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, TransactionId> {
}
