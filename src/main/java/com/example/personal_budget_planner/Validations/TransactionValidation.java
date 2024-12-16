package com.example.personal_budget_planner.Validations;

import com.example.personal_budget_planner.DTO.Request.TransactionRequest;
import com.example.personal_budget_planner.Exceptions.TransactionException;

public class TransactionValidation {

    public static void validateTransaction(TransactionRequest request) {
        if(request.getAmount()<0){
            throw new TransactionException("Amount should be greater than 0");
        }
    }
}
