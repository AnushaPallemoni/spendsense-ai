package com.anusha.spendsense_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParsedExpense(
        BigDecimal amount,
        String category,
        String description,
        LocalDate expenseDate
) {}
