package com.glzd.expenseTrackerApp.data;

import com.glzd.expenseTrackerApp.business.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;


public interface repo_expense extends JpaRepository<Expense, Long> {


    Page<Expense> findByDateBetweenOrderByCreationDateDesc(LocalDate startDate, LocalDate endDate, Pageable page);


    Page<Expense> findByExpenseTypeOrderByCreationDateDesc(String expenseType, Pageable page);


    Page<Expense> findByDateBetweenAndExpenseTypeOrderByCreationDateDesc(LocalDate startDate, LocalDate endDate, String expenseType, Pageable page);
}
