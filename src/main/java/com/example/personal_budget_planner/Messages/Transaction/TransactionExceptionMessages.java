package com.example.personal_budget_planner.Messages.Transaction;

public class TransactionExceptionMessages {

    public static final String UNABLE_SAVE_TRANSACTION = "Unable to save the transaction. Reason - %s";
    public static final String UNABLE_DELETE_TRANSACTION = "Unable to delete the transaction. Reason - %s";
    public static final String UNABLE_UPDATE_TRANSACTION = "Unable to update the transaction. Reason - %s";
    public static final String TRANSACTION_NOT_FOUND = "Transaction is not found with id - %s";
    public static final String INVALID_TRANSACTION_ENTITY = "Transaction entity cannot be null or empty";
    public static final String INVALID_TRANSACTION_AMOUNT = "Amount should be greater than 0";
    public static final String INVALID_TRANSACTION_CATEGORY = "Category cannot be null or empty";
    public static final String INVALID_TRANSACTION_TYPE = "Transaction type cannot be null or empty";
    public static final String UNABLE_TO_PARSE_DATE = "Unable to parse the date ";
}
