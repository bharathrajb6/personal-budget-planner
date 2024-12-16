package com.example.personal_budget_planner.Validations;

import com.example.personal_budget_planner.DTO.Request.UserRequest;
import com.example.personal_budget_planner.Exceptions.UserException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserValidation {

    /**
     * This method will validate the user details
     *
     * @param request
     */
    public static void validateUserDetails(UserRequest request) {

        if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
            throw new UserException("First Name cannot be null");
        }
        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            throw new UserException("Last Name cannot be empty");
        }

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new UserException("Email cannot be empty");
        }

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new UserException("username cannot be empty");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new UserException("Password cannot be empty");
        }

        if (request.getContactNumber() == null || request.getContactNumber().isEmpty()) {
            throw new UserException("Contact Number cannot be empty");
        }

        if (request.getRole() == null) {
            throw new UserException("Role cannot be empty");
        }

    }
}
