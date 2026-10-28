package com.wallet.wallettracker.repository;

import com.wallet.wallettracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByWalletIdAndMonthAndYear(
            Long walletId,
            Integer month,
            Integer year
    );
}