package com.wallet.wallettracker.dto;

import java.math.BigDecimal;

public class ExpenseSummary {

    private String category;
    private BigDecimal totalAmount;

    public ExpenseSummary(String category, BigDecimal totalAmount) {
        this.category = category;
        this.totalAmount = totalAmount;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}