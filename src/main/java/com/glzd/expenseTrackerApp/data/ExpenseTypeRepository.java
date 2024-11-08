package com.glzd.expenseTrackerApp.data;

import com.glzd.expenseTrackerApp.business.model.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ExpenseTypeRepository extends JpaRepository<ExpenseType, Long> {


    @Query("SELECT COUNT(et) > 0 FROM ExpenseType et WHERE LOWER(et.expenseCategory) = LOWER(:expenseCategory)")
    boolean existsBy(String expenseCategory);
}
