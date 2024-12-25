package com.example.personal_budget_planner.Service;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.Model.TransactionType;

public interface SavingGoalService {

    SavingGoalResponse setGoal(SavingGoalRequest request);

    SavingGoalResponse getGoal();

    SavingGoalResponse updateGoal(SavingGoalRequest request);

    void deleteGoal(String goalID);

    void updateCurrentSavingsForNewTransaction(double amount, TransactionType operation);

    void updateCurrentSavingsForExistingTransaction(double amount, String username);

    boolean checkIfGoalIsPresentForUser(String username);

    void notifyUser(String username);
}
