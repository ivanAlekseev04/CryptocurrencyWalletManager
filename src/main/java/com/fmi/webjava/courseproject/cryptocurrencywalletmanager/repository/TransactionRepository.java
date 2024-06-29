package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.repository;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT * FROM transaction WHERE user_id=?1", nativeQuery = true)
    List<Transaction> findAllTransactionsForUser(Long userId);

    @Query(value = "SELECT * FROM transaction WHERE user_id=?1 AND type=?2", nativeQuery = true)
    List<Transaction> findTransactionsByType(Long userId, String type);

    @Query(value = "SELECT transaction_id, amount, date_of_commit, selling_profit, type, crypto.crypto_id, user_id FROM transaction JOIN crypto ON transaction.crypto_id = crypto.crypto_id WHERE user_id=?1 AND crypto.name=?2", nativeQuery = true)
    List<Transaction> findTransactionByAssetId(Long userId, String assetId);

    @Query(value = "SELECT transaction_id, amount, date_of_commit, selling_profit, type, crypto.crypto_id, user_id FROM transaction JOIN crypto ON transaction.crypto_id = crypto.crypto_id WHERE user_id=?1 AND crypto.name=?2 AND type=?3", nativeQuery = true)
    List<Transaction> findTransactionByAssetAndType(Long userId, String assetId, String type);

    @Query(value = "SELECT * FROM transaction WHERE user_id=?1 AND date_of_commit<?2", nativeQuery = true)
    List<Transaction> findTransactionBefore(Long userId, LocalDateTime date);

    @Query(value = "SELECT * FROM transaction WHERE user_id=?1 AND date_of_commit>?2", nativeQuery = true)
    List<Transaction> findTransactionAfter(Long userId, LocalDateTime date);

    @Query(value = "SELECT * FROM transaction WHERE user_id=?1 AND date_of_commit<?2 AND date_of_commit>?3", nativeQuery = true)
    List<Transaction> findTransactionBetween(Long userId, LocalDateTime before, LocalDateTime after);
}
