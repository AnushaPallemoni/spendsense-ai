package com.anusha.spendsense_backend.dto;

import com.anusha.spendsense_backend.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponse(
        Long id,
        BigDecimal amount,
        String category,
        String description,
        LocalDate expenseDate
) {
    public static ExpenseResponse from(Expense e) {
        return new ExpenseResponse(e.getId(), e.getAmount(), e.getCategory(),
                e.getDescription(), e.getExpenseDate());
    }
}
