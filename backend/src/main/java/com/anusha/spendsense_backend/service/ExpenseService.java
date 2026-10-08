package com.anusha.spendsense_backend.service;

import com.anusha.spendsense_backend.dto.ExpenseRequest;
import com.anusha.spendsense_backend.dto.ExpenseResponse;
import com.anusha.spendsense_backend.model.Expense;
import com.anusha.spendsense_backend.model.User;
import com.anusha.spendsense_backend.repository.ExpenseRepository;
import com.anusha.spendsense_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

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
}


