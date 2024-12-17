package com.example.personal_budget_planner.Validations;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.Exceptions.GoalException;

import static com.example.personal_budget_planner.Messages.SavingGoal.SavingGoalExceptionMessages.*;

public class GoalValidation {

    public static void validateGoalDetails(SavingGoalRequest request) {

        if (request.getCurrentSavings() < 0) {
            throw new GoalException(INVALID_GOAL_CURRENT_SAVINGS);
        }

        if (request.getMonthlyTarget() < 0) {
            throw new GoalException(INVALID_GOAL_MONTHLY_TARGET);
        }

        if (request.getYearlyTarget() < 0) {
            throw new GoalException(INVALID_GOAL_YEARLY_TARGET);
        }

    }
}
