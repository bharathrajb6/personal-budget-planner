package com.example.personal_budget_planner.DTO.Response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavingGoalResponse {
    private double monthlyTarget;
    private double yearlyTarget;
    private double currentSavings;
}
