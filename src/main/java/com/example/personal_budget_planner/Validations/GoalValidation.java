package com.example.personal_budget_planner.Validations;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.Exceptions.GoalException;

public class GoalValidation {

    public static void validateGoalDetails(SavingGoalRequest request) {

        if (request.getCurrentSavings() < 0) {
            throw new GoalException("Current savings cannot be less than zero");
        }

        if (request.getMonthlyTarget() < 0) {
            throw new GoalException("Monthly target cannot be less than zero");
        }

        if (request.getYearlyTarget() < 0) {
            throw new GoalException("Yearly target cannot be less than zero");
        }

    }
}
