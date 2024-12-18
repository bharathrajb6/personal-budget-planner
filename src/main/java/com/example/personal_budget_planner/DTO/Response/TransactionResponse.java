package com.example.personal_budget_planner.DTO.Response;

import com.example.personal_budget_planner.Model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private String transactionID;
    private String username;
    private double amount;
    private String category;
    private TransactionType type;
    private Timestamp transactionDate;
}
