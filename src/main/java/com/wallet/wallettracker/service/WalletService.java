
package com.wallet.wallettracker.service;

import com.wallet.wallettracker.entity.Wallet;
import com.wallet.wallettracker.entity.Transaction;
import com.wallet.wallettracker.repository.WalletRepository;
import com.wallet.wallettracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wallet.wallettracker.entity.Expense;
import com.wallet.wallettracker.repository.ExpenseRepository;
import com.wallet.wallettracker.dto.ExpenseSummary;


import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final ExpenseRepository expenseRepository;


    public WalletService(
            WalletRepository walletRepository,
            TransactionRepository transactionRepository,
            ExpenseRepository expenseRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.expenseRepository = expenseRepository;
    }

    public Wallet createWallet(String ownerName) {

        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("Owner name is required");
        }

        Wallet wallet = new Wallet();
        wallet.setOwnerName(ownerName);

        return walletRepository.save(wallet);
    }


    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }
    public Wallet getWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public BigDecimal getWalletBalance(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        return wallet.getBalance();
    }


    public List<Transaction> getTransactionHistory(Long walletId) {
        return transactionRepository
                .findByWalletIdOrderByCreatedAtDesc(walletId);
    }

    public List<Transaction> getMonthlyTransactions(
            Long walletId,
            java.time.LocalDateTime startDate,
            java.time.LocalDateTime endDate) {

        return transactionRepository.findMonthlyTransactions(
                walletId, startDate, endDate);
    }


    @Transactional
    public Expense addExpense(
            Long walletId,
            String category,
            String description,
            BigDecimal amount) {

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category is required");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Expense amount must be greater than zero");
        }

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        Expense expense = new Expense();
        expense.setWalletId(walletId);
        expense.setCategory(category);
        expense.setDescription(description);
        expense.setAmount(amount);

    Expense savedExpense = expenseRepository.save(expense);
    expenseRepository.flush();

    Transaction transaction = new Transaction();
    transaction.setWalletId(walletId);
    transaction.setTransactionType("EXPENSE");
    transaction.setAmount(amount);
    transactionRepository.save(transaction);

    return savedExpense;

    }



    @Transactional
    public Wallet addMoney(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero");
        }

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        Transaction transaction = new Transaction();
        transaction.setWalletId(walletId);
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(amount);
        transactionRepository.save(transaction);

        return wallet;
    }

    public List<Expense> getExpenses(Long walletId) {
        return expenseRepository
                .findByWalletIdOrderByCreatedAtDesc(walletId);
    }
    public BigDecimal getTotalExpenses(Long walletId) {
        BigDecimal total = expenseRepository.getTotalExpensesByWalletId(walletId);

        return total == null ? BigDecimal.ZERO : total;
    }

    public List<Expense> getMonthlyExpenses(
            Long walletId,
            java.time.LocalDateTime startDate,
            java.time.LocalDateTime endDate) {

        return expenseRepository.findExpensesByWalletIdAndDateRange(
                walletId, startDate, endDate);
    }

    public BigDecimal getMonthlyExpenseTotal(
            Long walletId,
            java.time.LocalDateTime startDate,
            java.time.LocalDateTime endDate) {

        BigDecimal total = expenseRepository.getMonthlyExpensesByWalletId(
                walletId, startDate, endDate);

        return total == null ? BigDecimal.ZERO : total;
    }

    public List<ExpenseSummary> getExpenseSummaryByCategory(Long walletId) {

        // Check whether the wallet exists
        walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        // Return the expense summary
        return expenseRepository.getExpenseSummaryDtoByWalletId(walletId);

    }

    @Transactional
    public Wallet withdrawMoney(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Withdrawal amount must be greater than zero");
        }

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        Transaction transaction = new Transaction();
        transaction.setWalletId(walletId);
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setAmount(amount);
        transactionRepository.save(transaction);

        return wallet;
    }
}
