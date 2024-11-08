package com.glzd.expenseTrackerApp.business.services;

import com.glzd.expenseTrackerApp.business.model.Expense;
import com.glzd.expenseTrackerApp.data.repo_expense;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.stream.StreamSupport;

@Service
public class serviceExpenses {
    private final repo_expense repo_Expense;

    public serviceExpenses(repo_expense repo_Expense) {
        this.repo_Expense = repo_Expense;
    }

    public Expense save(Expense entity) {
        return repo_Expense.save(entity);
    }


    public Expense findById(Long id) {
        return repo_Expense.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sorry, the content you are looking for does not exist."));
    }



    public Page<Expense> findAll(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("creationDate").descending());

        return repo_Expense.findAll(sortedPageable);
    }


    public Iterable<Expense> findAll() {
        return repo_Expense.findAll();
    }

    public void deleteById(Long id) {
        Expense expenseToBeDeleted = findById(id);
        repo_Expense.delete(expenseToBeDeleted);
    }


    public BigDecimal getTotalAmount(Iterable<Expense> expenses){
        return StreamSupport.
                stream(expenses.spliterator(), false)
                .toList()
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public Page<Expense> getExpensesByYearMonthAndType(int year, Month month, String expenseType, Pageable page) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return repo_Expense.findByDateBetweenAndExpenseTypeOrderByCreationDateDesc(startDate, endDate, expenseType, page);
    }


    public Page<Expense> getExpensesByYearMonth(int year, Month month, Pageable page) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return repo_Expense.findByDateBetweenOrderByCreationDateDesc(startDate, endDate, page);
    }



    public Page<Expense> getExpensesByType(String expenseType, Pageable page) {
        return repo_Expense.findByExpenseTypeOrderByCreationDateDesc(expenseType, page);
    }


    public String convertToCSV(Iterable<Expense> expenses) {
        StringBuilder expensesAsCSV = new StringBuilder();
        expensesAsCSV.append("Id,Name of Expense,Type of expense,Amount,Date,Creation Timestamp\n");

        for (Expense expense: expenses) {
            expensesAsCSV.append(expense.getId()).append(",")
                    .append(expense.getName()).append(",")
                    .append(expense.getExpenseType()).append(",")
                    .append(expense.getAmount()).append(",")
                    .append(expense.getDate()).append(",")
                    .append(expense.getCreationDate()).append("\n");
        }

        return expensesAsCSV.toString();
    }

}
