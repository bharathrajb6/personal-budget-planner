package com.example.personal_budget_planner.Service;

import com.example.personal_budget_planner.DTO.Request.TransactionRequest;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;

public interface TransactionService {

    TransactionResponse addTransaction(TransactionRequest request);

    TransactionResponse getTransaction(String transactionID);

    void deleteTransaction(String transactionID);
}
