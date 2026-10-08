package com.anusha.spendsense_backend.repository;

import com.anusha.spendsense_backend.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByOwnerEmailOrderByExpenseDateDescIdDesc(String email);

    Optional<Expense> findByIdAndOwnerEmail(Long id, String email);
}
