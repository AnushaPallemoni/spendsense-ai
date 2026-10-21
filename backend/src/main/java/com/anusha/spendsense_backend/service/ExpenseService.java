package com.anusha.spendsense_backend.service;

import lombok.extern.slf4j.Slf4j;

import com.anusha.spendsense_backend.dto.ExpenseRequest;
import com.anusha.spendsense_backend.dto.ExpenseResponse;
import com.anusha.spendsense_backend.dto.ParsedExpense;
import com.anusha.spendsense_backend.model.Expense;
import com.anusha.spendsense_backend.model.User;
import com.anusha.spendsense_backend.repository.ExpenseRepository;
import com.anusha.spendsense_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private static final List<String> CATEGORIES =
            List.of("Food", "Travel", "Shopping", "Bills", "Entertainment", "Health", "Other");

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final AiExpenseParser aiExpenseParser;

    public ExpenseResponse create(String email, ExpenseRequest request) {
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        Expense expense = Expense.builder()
                .amount(request.amount())
                .category(request.category())
                .description(request.description())
                .expenseDate(request.expenseDate() != null
                        ? request.expenseDate()
                        : LocalDate.now())
                .owner(owner)
                .build();

        return ExpenseResponse.from(expenseRepository.save(expense));
    }

    public ExpenseResponse createFromText(String email, String text) {
        ParsedExpense parsed;
        try {
            parsed = aiExpenseParser.parse(text);
        } catch (RuntimeException e) {
            log.error("AI parsing failed", e);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "AI service is unavailable, please try again");
        }

        if (parsed == null || parsed.amount() == null || parsed.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Could not find an amount in your text");
        }

        LocalDate today = LocalDate.now();
        LocalDate date = (parsed.expenseDate() == null || parsed.expenseDate().isAfter(today))
                ? today
                : parsed.expenseDate();

        String description = parsed.description() == null ? "" : parsed.description().trim();
        if (description.length() > 100) {
            description = description.substring(0, 100);
        }

        return create(email, new ExpenseRequest(
                parsed.amount(), normalizeCategory(parsed.category()), description, date));
    }

    public List<ExpenseResponse> findAll(String email) {
        return expenseRepository.findByOwnerEmailOrderByExpenseDateDescIdDesc(email)
                .stream()
                .map(ExpenseResponse::from)
                .toList();
    }

    public void delete(String email, Long id) {
        Expense expense = expenseRepository.findByIdAndOwnerEmail(id, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));
        expenseRepository.delete(expense);
    }

    private String normalizeCategory(String raw) {
        if (raw == null) {
            return "Other";
        }
        return CATEGORIES.stream()
                .filter(c -> c.equalsIgnoreCase(raw.trim()))
                .findFirst()
                .orElse("Other");
    }
}




