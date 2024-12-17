package com.example.personal_budget_planner.Messages.User;

public class UserExceptionMessages {
    public static final String USER_NOT_FOUND = "User not found - %s";
    public static final String USER_ENTITY_CANNOT_NULL = "User entity cannot be null";
    public static final String PASSWORD_CANNOT_NULL = "Password cannot be null or empty";
    public static final String USER_ALREADY_EXISTS = "User %s already exists";
    public static final String UNABLE_TO_SAVE_USER_DATA = "Unable to save user %s details. Reason - %s";
    public static final String UNABLE_TO_SAVE_JWT_TOKEN = "Unable to save JWT token. Reason - %s";
    public static final String UNABLE_TO_DELETE_ALL_OLD_TOKENS = "Unable to delete all existing tokens for user %s. Reason - %s";
    public static final String INVALID_FIRST_NAME = "First Name cannot be null or empty";
    public static final String INVALID_LAST_NAME = "Last Name cannot be null or empty";
    public static final String INVALID_EMAIL = "Email cannot be null or empty";
    public static final String INVALID_USERNAME = "Username cannot be null or empty";
    public static final String INVALID_CONTACT_NUMBER = "Contact Number cannot be null or empty";
    public static final String INVALID_ROLE = "Role cannot be null or empty";
}
