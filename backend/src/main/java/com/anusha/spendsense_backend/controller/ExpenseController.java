package com.anusha.spendsense_backend.controller;

import com.anusha.spendsense_backend.dto.ExpenseRequest;
import com.anusha.spendsense_backend.dto.ExpenseResponse;
import com.anusha.spendsense_backend.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse create(@Valid @RequestBody ExpenseRequest request,
                                  Authentication authentication) {
        return expenseService.create(authentication.getName(), request);
    }

    @GetMapping
    public List<ExpenseResponse> list(Authentication authentication) {
        return expenseService.findAll(authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication authentication) {
        expenseService.delete(authentication.getName(), id);
    }
}
