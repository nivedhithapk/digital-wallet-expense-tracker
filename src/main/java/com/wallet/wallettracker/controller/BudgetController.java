
package com.wallet.wallettracker.controller;

import com.wallet.wallettracker.entity.Budget;
import com.wallet.wallettracker.service.BudgetService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "http://localhost:5174")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public Budget saveBudget(
            @RequestParam Long walletId,
            @RequestParam Integer month,
            @RequestParam Integer year,
            @RequestParam BigDecimal amount) {

        return budgetService.saveOrUpdateBudget(
                walletId, month, year, amount);
    }

    @GetMapping
    public Budget getBudget(
            @RequestParam Long walletId,
            @RequestParam Integer month,
            @RequestParam Integer year) {

        return budgetService.getBudget(walletId, month, year);
    }
}

