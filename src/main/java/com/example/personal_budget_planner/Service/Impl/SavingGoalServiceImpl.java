package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.Service.BudgetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SavingGoalServiceImpl implements BudgetService {
    @Override
    public SavingGoalResponse setGoal(SavingGoalRequest request) {
        return null;
    }

    @Override
    public SavingGoalResponse getGoal(String goalID) {
        return null;
    }
}
