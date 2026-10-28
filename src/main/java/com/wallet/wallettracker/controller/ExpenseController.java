
package com.wallet.wallettracker.controller;

import com.wallet.wallettracker.entity.Expense;
import com.wallet.wallettracker.repository.ExpenseRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class ExpenseController {

    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @PostMapping
    public Expense addExpense(@RequestBody Expense expense) {

        if (expense.getAmount() == null ||
                expense.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Expense amount must be greater than zero"
            );
        }

        if (expense.getCategory() == null ||
                expense.getCategory().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Expense category is required"
            );
        }

        if (expense.getWalletId() == null) {
            expense.setWalletId(1L);
        }

        return expenseRepository.save(expense);
    }

    @GetMapping("/wallet/{walletId}")
    public List<Expense> getExpenses(@PathVariable Long walletId) {
        return expenseRepository.findByWalletIdOrderByCreatedAtDesc(walletId);
    }
}

