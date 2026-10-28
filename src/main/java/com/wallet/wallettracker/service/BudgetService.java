package com.wallet.wallettracker.service;

import com.wallet.wallettracker.entity.Budget;
import com.wallet.wallettracker.repository.BudgetRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget saveOrUpdateBudget(
            Long walletId,
            Integer month,
            Integer year,
            BigDecimal amount) {

        Budget budget = budgetRepository
                .findByWalletIdAndMonthAndYear(walletId, month, year)
                .orElse(new Budget());

        budget.setWalletId(walletId);
        budget.setMonth(month);
        budget.setYear(year);
        budget.setAmount(amount);

        return budgetRepository.save(budget);
    }

    public Budget getBudget(
            Long walletId,
            Integer month,
            Integer year) {

        return budgetRepository
                .findByWalletIdAndMonthAndYear(walletId, month, year)
                .orElse(null);
    }
}
