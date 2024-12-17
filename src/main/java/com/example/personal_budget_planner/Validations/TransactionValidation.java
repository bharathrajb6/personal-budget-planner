package com.example.personal_budget_planner.Validations;

import com.example.personal_budget_planner.DTO.Request.TransactionRequest;
import com.example.personal_budget_planner.Exceptions.TransactionException;
import com.example.personal_budget_planner.Model.ExpenseCategory;
import com.example.personal_budget_planner.Model.IncomeCategory;
import com.example.personal_budget_planner.Model.TransactionType;

import static com.example.personal_budget_planner.Messages.Transaction.TransactionExceptionMessages.*;

public class TransactionValidation {

    public static void validateTransaction(TransactionRequest request) {

        if (request == null) {
            throw new TransactionException(INVALID_TRANSACTION_ENTITY);
        }

        if (request.getAmount() < 0) {
            throw new TransactionException(INVALID_TRANSACTION_AMOUNT);
        }

        if (request.getCategory() == null || request.getCategory().isEmpty()) {
            throw new TransactionException(INVALID_TRANSACTION_CATEGORY);
        }

        if (request.getType() == null || request.getType().toString().isEmpty()) {
            throw new TransactionException(INVALID_TRANSACTION_TYPE);
        }

        TransactionType type = request.getType();
        String category = request.getCategory();
        if (type == TransactionType.INCOME) {
            try {
                IncomeCategory.valueOf(category);
            } catch (IllegalArgumentException exception) {
                throw new TransactionException(exception.getMessage());
            }
        }
        if (type == TransactionType.EXPENSE) {
            try {
                ExpenseCategory.valueOf(category);
            } catch (IllegalArgumentException exception) {
                throw new TransactionException(exception.getMessage());
            }
        }

    }
}
