package com.anusha.spendsense_backend.service;

import com.anusha.spendsense_backend.dto.ExpenseRequest;
import com.anusha.spendsense_backend.model.Expense;
import com.anusha.spendsense_backend.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void create_usesTodayWhenDateIsMissing() {
        when(expenseRepository.save(any(Expense.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ExpenseRequest request =
                new ExpenseRequest(new BigDecimal("250"), "Food", "biryani", null);

        expenseService.create(request);

        ArgumentCaptor<Expense> captor = ArgumentCaptor.forClass(Expense.class);
        verify(expenseRepository).save(captor.capture());
        assertEquals(LocalDate.now(), captor.getValue().getExpenseDate());
        assertEquals("Food", captor.getValue().getCategory());
    }
}