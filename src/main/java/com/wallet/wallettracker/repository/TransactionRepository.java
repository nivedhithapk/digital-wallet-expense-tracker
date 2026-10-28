package com.wallet.wallettracker.repository;
import com.wallet.wallettracker.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByWalletIdOrderByCreatedAtDesc(Long walletId);


    @Query("SELECT t FROM Transaction t " +
            "WHERE t.walletId = :walletId " +
            "AND t.createdAt >= :startDate " +
            "AND t.createdAt < :endDate " +
            "ORDER BY t.createdAt DESC")
    List<Transaction> findMonthlyTransactions(
            @Param("walletId") Long walletId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}