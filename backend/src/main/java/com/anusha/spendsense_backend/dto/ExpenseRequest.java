package com.anusha.spendsense_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
        @NotNull @Positive BigDecimal amount,
        @NotBlank String category,
        String description,
        LocalDate expenseDate
) {}
