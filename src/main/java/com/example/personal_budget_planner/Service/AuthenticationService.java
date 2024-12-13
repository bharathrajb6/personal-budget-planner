package com.example.personal_budget_planner.Service;

import com.example.personal_budget_planner.DTO.Request.UserRequest;

public interface AuthenticationService {
    String register(UserRequest request);
    String login(UserRequest request);
}
