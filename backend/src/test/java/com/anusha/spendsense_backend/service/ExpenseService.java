package com.anusha.spendsense_backend.service;

import com.anusha.spendsense_backend.dto.ExpenseRequest;
import com.anusha.spendsense_backend.dto.ExpenseResponse;
import com.anusha.spendsense_backend.model.Expense;
import com.anusha.spendsense_backend.model.User;
import com.anusha.spendsense_backend.repository.ExpenseRepository;
import com.anusha.spendsense_backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void create_usesTodayWhenDateIsMissing() {
        User user = User.builder().id(1L).email("a@example.com").name("A").password("x").build();
        when(userRepository.findByEmail("a@example.com")).thenReturn(Optional.of(user));
        when(expenseRepository.save(any(Expense.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ExpenseRequest request =
                new ExpenseRequest(new BigDecimal("250"), "Food", "biryani", null);

        ExpenseResponse response = expenseService.create("a@example.com", request);

        assertEquals(LocalDate.now(), response.expenseDate());
        assertEquals("Food", response.category());
    }

    @Test
    void delete_returnsNotFoundForSomeoneElsesExpense() {
        when(expenseRepository.findByIdAndOwnerEmail(5L, "a@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> expenseService.delete("a@example.com", 5L));
    }
}
