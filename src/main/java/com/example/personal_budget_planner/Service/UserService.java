package com.example.personal_budget_planner.Service;

import com.example.personal_budget_planner.DTO.Request.UserRequest;
import com.example.personal_budget_planner.DTO.Response.UserResponse;

public interface UserService {

    String getUsername();

    UserResponse getUserDetails();

    UserResponse updateUserDetails(UserRequest userRequest);

    UserResponse updateUserPassword(UserRequest userRequest);
}
