package com.anusha.spendsense_backend.controller;

import com.anusha.spendsense_backend.dto.ExpenseRequest;
import com.anusha.spendsense_backend.model.Expense;
import com.anusha.spendsense_backend.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Expense create(@Valid @RequestBody ExpenseRequest request) {
        return expenseService.create(request);
    }

    @GetMapping
    public List<Expense> list() {
        return expenseService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        expenseService.delete(id);
    }
}
