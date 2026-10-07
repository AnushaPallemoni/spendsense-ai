package com.anusha.spendsense_backend.repository;

import com.anusha.spendsense_backend.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
