package com.example.personal_budget_planner.Service;

import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.DTO.Response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AdminService {

    UserResponse getUserData(String username);

    Page<UserResponse> getAllUsersData(Pageable pageable);

    TransactionResponse getTransactionByID(String transactionID);

    Page<TransactionResponse> getAllTransaction(Pageable pageable);

    Page<TransactionResponse> getAllTransactionForUser(String username, Pageable pageable);

    SavingGoalResponse getGoalDetails(String goalID);

    Page<SavingGoalResponse> getAllGoals(Pageable pageable);
}
