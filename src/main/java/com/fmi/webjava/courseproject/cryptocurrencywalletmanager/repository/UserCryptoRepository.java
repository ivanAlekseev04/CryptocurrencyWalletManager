package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.repository;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto.UserCrypto;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto.UserCryptoId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Transactional
@Repository
public interface UserCryptoRepository extends JpaRepository<UserCrypto, UserCryptoId> {
    @Query(value = "SELECT * FROM user_crypto WHERE crypto_name = ?1 AND user_id = ?2", nativeQuery = true)
    Optional<UserCrypto> findByCryptoNameAndUserId(String cryptoName, Long userId);

    @Query(value = "SELECT * FROM user_crypto WHERE user_id = ?1", nativeQuery = true)
    Optional<Set<UserCrypto>> findByUserId(Long userId);
}
