package com.example.personal_budget_planner.Service;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.Model.TransactionType;

public interface SavingGoalInterface {

    SavingGoalResponse setGoal(SavingGoalRequest request);

    SavingGoalResponse getGoal(String goalID);

    SavingGoalResponse updateGoal(String goalID, SavingGoalRequest request);

    void updateCurrentSavings(double amount, TransactionType operation);
}
