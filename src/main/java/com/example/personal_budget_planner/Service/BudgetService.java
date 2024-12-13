package com.example.personal_budget_planner.Service;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;

public interface BudgetService {

    SavingGoalResponse setGoal(SavingGoalRequest request);

    SavingGoalResponse getGoal(String goalID);
}
