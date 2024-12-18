package com.example.personal_budget_planner.Validations;

import com.example.personal_budget_planner.DTO.Request.UserRequest;
import com.example.personal_budget_planner.Exceptions.UserException;
import com.example.personal_budget_planner.Model.Role;
import lombok.extern.slf4j.Slf4j;

import static com.example.personal_budget_planner.Messages.User.UserExceptionMessages.*;

@Slf4j
public class UserValidation {

    /**
     * This method will validate the user details
     *
     * @param request
     */
    public static void validateUserDetails(UserRequest request) {

        if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
            throw new UserException(INVALID_FIRST_NAME);
        }
        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            throw new UserException(INVALID_LAST_NAME);
        }

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new UserException(INVALID_EMAIL);
        }

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new UserException(INVALID_USERNAME);
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new UserException(PASSWORD_CANNOT_NULL);
        }

        if (request.getContactNumber() == null || request.getContactNumber().isEmpty()) {
            throw new UserException(INVALID_CONTACT_NUMBER);
        }

        if (request.getRole() == null) {
            throw new UserException(INVALID_ROLE);
        }

        try {
            Role.valueOf(request.getRole().toString());
        } catch (Exception exception) {
            throw new UserException(INVALID_ROLE_VALUE);
        }

    }
}
