package com.example.personal_budget_planner.DTO.Request;

import com.example.personal_budget_planner.Model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {
    private double amount;
    private String category;
    private TransactionType type;
}
