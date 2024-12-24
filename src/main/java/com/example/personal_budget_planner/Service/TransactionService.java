package com.example.personal_budget_planner.Service;

import com.example.personal_budget_planner.DTO.Request.TransactionRequest;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    TransactionResponse addTransaction(TransactionRequest request);

    TransactionResponse getTransaction(String transactionID);

    void deleteTransaction(String transactionID);

    Page<TransactionResponse> getAllTransactionForUser(Pageable pageable);

    TransactionResponse updateTransaction(String transactionID, TransactionRequest request);

    Page<TransactionResponse> getFilteredTransaction(String start, String end, Pageable pageable);

    Page<TransactionResponse> getTransactionsListByCategory(String category, Pageable pageable);

    double getTransactionAmountByCategory(String category);
}
