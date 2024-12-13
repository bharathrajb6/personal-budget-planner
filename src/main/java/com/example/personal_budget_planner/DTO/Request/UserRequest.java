package com.example.personal_budget_planner.DTO.Request;

import com.example.personal_budget_planner.Model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String contactNumber;
    private Role role;
}
