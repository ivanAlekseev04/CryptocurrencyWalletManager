package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.repository;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.Crypto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface CryptoRepository extends JpaRepository<Crypto, String> {
}
