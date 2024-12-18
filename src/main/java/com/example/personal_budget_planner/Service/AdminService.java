package com.example.personal_budget_planner.Service;

import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.DTO.Response.UserResponse;

import java.util.List;

public interface AdminService {

    UserResponse getUserData(String username);

    List<UserResponse> getAllUsersData();

    TransactionResponse getTransactionByID(String transactionID);

    List<TransactionResponse> getAllTransaction();

    List<TransactionResponse> getAllTransactionForUser(String username);

    SavingGoalResponse getGoalDetails(String goalID);

    List<SavingGoalResponse> getAllGoals();
}
