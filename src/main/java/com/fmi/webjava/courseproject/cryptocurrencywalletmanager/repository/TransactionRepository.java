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
    @Query(value = "SELECT * FROM transaction WHERE user_id=?1 ORDER BY date_of_commit DESC", nativeQuery = true)
    List<Transaction> findAllTransactionsForUser(Long userId);

    @Query(value = "SELECT * FROM transaction WHERE user_id=?1 AND type=?2 ORDER BY date_of_commit DESC", nativeQuery = true)
    List<Transaction> findTransactionsByType(Long userId, String type);

    @Query(value = "SELECT transaction_id, amount, date_of_commit, selling_profit, type, crypto.crypto_id, user_id FROM transaction JOIN crypto ON transaction.crypto_id = crypto.crypto_id WHERE user_id=?1 AND crypto.name=?2 ORDER BY date_of_commit DESC", nativeQuery = true)
    List<Transaction> findTransactionsByAssetId(Long userId, String assetId);

    @Query(value = "SELECT transaction_id, amount, date_of_commit, selling_profit, type, crypto.crypto_id, user_id FROM transaction JOIN crypto ON transaction.crypto_id = crypto.crypto_id WHERE user_id=?1 AND crypto.name=?2 AND type=?3 ORDER BY date_of_commit DESC", nativeQuery = true)
    List<Transaction> findTransactionsByAssetAndType(Long userId, String assetId, String type);

    @Query(value = "SELECT * FROM transaction WHERE user_id=?1 AND date_of_commit<?2 ORDER BY date_of_commit DESC", nativeQuery = true)
    List<Transaction> findTransactionsBefore(Long userId, LocalDateTime date);

    @Query(value = "SELECT * FROM transaction WHERE user_id=?1 AND date_of_commit>?2 ORDER BY date_of_commit DESC", nativeQuery = true)
    List<Transaction> findTransactionsAfter(Long userId, LocalDateTime date);

    @Query(value = "SELECT * FROM transaction WHERE user_id=?1 AND date_of_commit<?2 AND date_of_commit>?3 ORDER BY date_of_commit DESC", nativeQuery = true)
    List<Transaction> findTransactionsBetween(Long userId, LocalDateTime before, LocalDateTime after);
}
