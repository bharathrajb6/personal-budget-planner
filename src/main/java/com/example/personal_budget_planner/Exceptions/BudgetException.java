package com.example.personal_budget_planner.Exceptions;

public class BudgetException extends RuntimeException{
    public BudgetException(String message) {
        super(message);
    }

    public BudgetException(String message, Throwable cause) {
        super(message, cause);
    }
}
