package com.example.personal_budget_planner.Exceptions;

public class GoalException extends RuntimeException {
    public GoalException(String message) {
        super(message);
    }

    public GoalException(String message, Throwable cause) {
        super(message, cause);
    }
}
