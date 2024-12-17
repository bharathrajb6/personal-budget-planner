package com.example.personal_budget_planner.Messages.User;

public class UserLogMessages {
    public static final String USER_IS_ALREADY_EXISTS = "User %s already exists in database";
    public static final String USER_DATA_SAVED_SUCCESSFULLY = "User %s data has been saved successfully";
    public static final String UNABLE_TO_SAVE_USER_DATA_IN_DB = "Unable to save user %s details. Reason - %s";
    public static final String USER_DATA_NOT_FOUND = "User not found - %s";
    public static final String USER_DATA_CANNOT_NULL = "User entity cannot be null";
    public static final String JWT_TOKEN_FOR_USER_SAVED_SUCCESS = "JWT Token for user %s has been saved successfully";
    public static final String UNABLE_TO_SAVE_JWT_TOKEN_TO_DB = "Unable to save JWT token. Reason - %s";
    public static final String JWT_TOKEN_FOR_USER_DELETED_SUCCESS = "All existing JWT token for user %s has been deleted successfully";
    public static final String USER_DATA_UPDATED_SUCCESSFULLY = "User %s data has been updated successfully";
    public static final String UNABLE_TO_UPDATE_USER_DATA = "Unable to update user %s data";
    public static final String USER_PASSWORD_UPDATED_SUCCESSFULLY = "User %s password has been updated successfully";
    public static final String UNABLE_TO_UPDATE_USER_PASSWORD = "Unable to update user password. Reason - %s";
}
