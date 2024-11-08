package com.glzd.expenseTrackerApp.business.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class ExpenseException extends Exception {
    public ExpenseException(String message) {
        super(message);
    }

}
