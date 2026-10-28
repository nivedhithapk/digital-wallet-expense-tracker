
package com.wallet.wallettracker.repository;

import com.wallet.wallettracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import com.wallet.wallettracker.dto.ExpenseSummary;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByWalletIdOrderByCreatedAtDesc(Long walletId);
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.walletId = :walletId")
    BigDecimal getTotalExpensesByWalletId(@Param("walletId") Long walletId);

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e WHERE e.walletId = :walletId GROUP BY e.category")
    List<Object[]> getExpenseSummaryByCategory(@Param("walletId") Long walletId);
    @Query("SELECT new com.wallet.wallettracker.dto.ExpenseSummary(e.category, SUM(e.amount)) " +
            "FROM Expense e WHERE e.walletId = :walletId GROUP BY e.category")
    List<ExpenseSummary> getExpenseSummaryDtoByWalletId(
            @Param("walletId") Long walletId);


    @Query("SELECT e FROM Expense e WHERE e.walletId = :walletId " +
            "AND e.createdAt >= :startDate AND e.createdAt < :endDate " +
            "ORDER BY e.createdAt DESC")
    List<Expense> findExpensesByWalletIdAndDateRange(
            @Param("walletId") Long walletId,
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
            "WHERE e.walletId = :walletId " +
            "AND e.createdAt >= :startDate AND e.createdAt < :endDate")
    BigDecimal getMonthlyExpensesByWalletId(
            @Param("walletId") Long walletId,
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate);

}

