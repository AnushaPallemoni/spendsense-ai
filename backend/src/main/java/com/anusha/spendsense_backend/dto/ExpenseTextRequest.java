package com.anusha.spendsense_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExpenseTextRequest(
        @NotBlank @Size(max = 300) String text
) {}
