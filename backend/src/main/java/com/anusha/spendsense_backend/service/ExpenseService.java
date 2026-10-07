package com.anusha.spendsense_backend.service;

import com.anusha.spendsense_backend.dto.ExpenseRequest;
import com.anusha.spendsense_backend.model.Expense;
import com.anusha.spendsense_backend.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public Expense create(ExpenseRequest request) {
        Expense expense = Expense.builder()
                .amount(request.amount())
                .category(request.category())
                .description(request.description())
                .expenseDate(request.expenseDate() != null
                        ? request.expenseDate()
                        : LocalDate.now())
                .build();
        return expenseRepository.save(expense);
    }

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    public void delete(Long id) {
        expenseRepository.deleteById(id);
    }
}

