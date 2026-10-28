package com.wallet.wallettracker.controller;

import com.wallet.wallettracker.entity.Wallet;
import com.wallet.wallettracker.service.WalletService;
import org.springframework.web.bind.annotation.*;

import com.wallet.wallettracker.entity.Transaction;
import com.wallet.wallettracker.entity.Expense;

import com.wallet.wallettracker.entity.Expense;
import com.wallet.wallettracker.dto.ExpenseSummary;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5175")
@RestController
@RequestMapping("/api/wallets")

public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public Wallet createWallet(@RequestBody Wallet wallet) {
        return walletService.createWallet(wallet.getOwnerName());
    }

    @GetMapping
    public List<Wallet> getAllWallets() {
        return walletService.getAllWallets();
    }
    @GetMapping("/{id}")
    public Wallet getWalletById(@PathVariable Long id) {
        return walletService.getWalletById(id);
    }
    @GetMapping("/{id}/balance")
    public java.math.BigDecimal getWalletBalance(@PathVariable Long id) {
        return walletService.getWalletBalance(id);
    }


    @PostMapping("/{id}/deposit")
    public Wallet addMoney(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, java.math.BigDecimal> request) {

        java.math.BigDecimal amount = request.get("amount");

        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        return walletService.addMoney(id, amount);
    }

    @GetMapping("/{id}/expenses")
    public List<Expense> getExpenses(@PathVariable Long id) {
        return walletService.getExpenses(id);
    }
    @GetMapping("/{id}/expenses/total")
    public java.math.BigDecimal getTotalExpenses(@PathVariable Long id) {
        return walletService.getTotalExpenses(id);
    }
    @GetMapping("/{id}/expenses/summary")
    public List<ExpenseSummary> getExpenseSummaryByCategory(@PathVariable Long id) {
        return walletService.getExpenseSummaryByCategory(id);
    }

    @GetMapping("/{id}/expenses/monthly")
    public List<Expense> getMonthlyExpenses(
            @PathVariable Long id,
            @RequestParam int year,
            @RequestParam int month) {

        java.time.LocalDateTime startDate =
                java.time.LocalDate.of(year, month, 1).atStartOfDay();

        java.time.LocalDateTime endDate =
                java.time.LocalDate.of(year, month, 1)
                        .plusMonths(1)
                        .atStartOfDay();

        return walletService.getMonthlyExpenses(id, startDate, endDate);
    }

    @GetMapping("/{id}/expenses/monthly/total")
    public java.math.BigDecimal getMonthlyExpenseTotal(
            @PathVariable Long id,
            @RequestParam int year,
            @RequestParam int month) {

        java.time.LocalDateTime startDate =
                java.time.LocalDate.of(year, month, 1).atStartOfDay();

        java.time.LocalDateTime endDate =
                java.time.LocalDate.of(year, month, 1)
                        .plusMonths(1)
                        .atStartOfDay();

        return walletService.getMonthlyExpenseTotal(id, startDate, endDate);
    }


    @PostMapping("/{id}/withdraw")
    public Wallet withdrawMoney(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, java.math.BigDecimal> request) {

        java.math.BigDecimal amount = request.get("amount");

        return walletService.withdrawMoney(id, amount);
    }

    @GetMapping("/{id}/transactions")
    public List<Transaction> getTransactionHistory(@PathVariable Long id) {
        return walletService.getTransactionHistory(id);
    }

    @GetMapping("/{id}/transactions/monthly")
    public List<Transaction> getMonthlyTransactions(
            @PathVariable Long id,
            @RequestParam int year,
            @RequestParam int month) {

        java.time.LocalDateTime startDate =
                java.time.LocalDate.of(year, month, 1).atStartOfDay();

        java.time.LocalDateTime endDate =
                java.time.LocalDate.of(year, month, 1)
                        .plusMonths(1)
                        .atStartOfDay();

        return walletService.getMonthlyTransactions(
                id, startDate, endDate);
    }



    @PostMapping("/{id}/expenses")
    public Expense addExpense(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, Object> request) {

        String category = (String) request.get("category");
        String description = (String) request.get("description");

        java.math.BigDecimal amount =
                new java.math.BigDecimal(request.get("amount").toString());

        return walletService.addExpense(id, category, description, amount);
    }


}
