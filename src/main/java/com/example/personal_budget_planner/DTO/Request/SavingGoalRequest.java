package com.example.personal_budget_planner.DTO.Request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingGoalRequest {
    private double monthlyTarget;
    private double yearlyTarget;
    private double currentSavings;
}
