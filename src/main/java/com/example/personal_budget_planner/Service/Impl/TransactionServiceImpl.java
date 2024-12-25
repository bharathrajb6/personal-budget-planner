package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Request.TransactionRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.DTO.Response.UserResponse;
import com.example.personal_budget_planner.Exceptions.TransactionException;
import com.example.personal_budget_planner.Mapper.TransactionMapper;
import com.example.personal_budget_planner.Model.ExpenseCategory;
import com.example.personal_budget_planner.Model.Transaction;
import com.example.personal_budget_planner.Model.TransactionType;
import com.example.personal_budget_planner.Repository.TransactionRepository;
import com.example.personal_budget_planner.Service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

import static com.example.personal_budget_planner.Messages.Transaction.TransactionExceptionMessages.*;
import static com.example.personal_budget_planner.Messages.Transaction.TransactionLogMessages.*;
import static com.example.personal_budget_planner.Validations.TransactionValidation.validateTransaction;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final SavingGoalService savingGoalService;
    private final UserService userService;
    private final RedisService redisService;
    private final EmailService emailService;

    /**
     * This method is used to add the transaction to database
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public TransactionResponse addTransaction(TransactionRequest request) {

        // Validate the transaction request object
        validateTransaction(request);
        boolean isGoalPresent = savingGoalService.checkIfGoalIsPresentForUser(userService.getUsername());
        if (!isGoalPresent) {
            throw new TransactionException("Goal is not found for this user");
        }
        // Convert the request object to model
        Transaction transaction = generateTransaction(request);

        try {
            // Save the object to database
            transactionRepository.save(transaction);
            log.info(TRANSACTION_SAVED_SUCCESSFULLY);
        } catch (Exception exception) {
            log.error(String.format(UNABLE_TO_SAVE_TRANSACTION, exception.getMessage()));
            throw new TransactionException(String.format(UNABLE_SAVE_TRANSACTION, exception.getMessage()));
        }

        // Update the current savings amount based on transaction type
        savingGoalService.updateCurrentSavingsForNewTransaction(transaction.getAmount(), transaction.getType());

        UserResponse userResponse = userService.getUserDetails();
        String body = String.format("Transaction has been added successfully with ID - %s.%n%nThanks,%nTeam Personal Budget Team", transaction.getTransactionID());
        emailService.sendEmail(userResponse.getEmail(), "New transaction has been added", body);
        return getTransaction(transaction.getTransactionID());
    }

    /**
     * This method is used to fetch the transaction details from database using its ID
     *
     * @param transactionID
     * @return
     */
    @Override
    public TransactionResponse getTransaction(String transactionID) {
        TransactionResponse transactionResponse = redisService.getData(transactionID, TransactionResponse.class);
        if (transactionResponse != null) {
            return transactionResponse;
        } else {
            // Fetch the transaction details from database
            Transaction transaction = transactionRepository.findByTransactionID(transactionID).orElseThrow(() -> {
                // If any issue come, then throw the exception
                log.error(String.format(UNABLE_TO_FIND_TRANSACTION, transactionID));
                return new TransactionException(String.format(TRANSACTION_NOT_FOUND, transactionID));
            });
            transactionResponse = transactionMapper.toTransactionResponse(transaction);
            redisService.setData(transactionID, transactionResponse, 300L);
            return transactionResponse;
        }
    }

    /**
     * This method will fetch all the transaction for the user
     *
     * @return
     */
    @Override
    public Page<TransactionResponse> getAllTransactionForUser(Pageable pageable) {
        // Get all the transactions for specific user
        Page<Transaction> transactions = transactionRepository.findAllTransactionForUser(userService.getUsername(), pageable);
        return transactionMapper.toTransactionResponseList(transactions);
    }

    /**
     * This method will delete the transaction of the user
     *
     * @param transactionID
     */
    @Override
    @Transactional
    public void deleteTransaction(String transactionID) {
        try {
            // Delete the transaction by using its ID
            transactionRepository.deleteById(transactionID);
            redisService.deleteData(transactionID);
            log.info(String.format(TRANSACTION_DELETED_SUCCESSFULLY, transactionID));
        } catch (Exception exception) {
            // If any issue come, then throw the exception
            log.error(String.format(UNABLE_TO_DELETE_TRANSACTION, exception.getMessage()));
            throw new TransactionException(String.format(UNABLE_DELETE_TRANSACTION, exception.getMessage()));
        }
    }

    /**
     * This method will update the transaction
     *
     * @param transactionID
     * @param request
     * @return
     */
    @Override
    @Transactional
    public TransactionResponse updateTransaction(String transactionID, TransactionRequest request) {
        // Validate the transaction request object
        validateTransaction(request);

        // Find the transaction by using its id
        Transaction oldTransaction = transactionRepository.findByTransactionID(transactionID).orElseThrow(() -> new TransactionException(String.format(TRANSACTION_NOT_FOUND, transactionID)));

        if (oldTransaction != null) {

            try {
                // Update the transaction details
                transactionRepository.updateTransaction(request.getCategory(), request.getAmount(), request.getType(), transactionID);

                // Update the current savings in goal
                updateSavingsData(oldTransaction, request);
                log.info(String.format(TRANSACTION_UPDATED_SUCCESSFULLY, transactionID));
            } catch (Exception exception) {
                // If any issue come, then throw the exception
                log.error(String.format(UNABLE_TO_UPDATE_TRANSACTION, exception.getMessage()));
                throw new TransactionException(String.format(UNABLE_UPDATE_TRANSACTION, exception.getMessage()));
            }
            redisService.deleteData(transactionID);
            return getTransaction(transactionID);
        } else {
            // If any issue come, then throw the exception
            log.error(String.format(UNABLE_TO_FIND_TRANSACTION, transactionID));
            throw new TransactionException(String.format(TRANSACTION_NOT_FOUND, transactionID));
        }
    }


    /**
     * This method is used to generate the transaction object from request object
     *
     * @param request
     * @return
     */
    private Transaction generateTransaction(TransactionRequest request) {
        // Convert the request object to transaction model
        Transaction transaction = transactionMapper.toTransaction(request);

        // Generate the id for transaction object
        transaction.setTransactionID(UUID.randomUUID().toString());
        transaction.setUsername(userService.getUsername());

        // Set the current time for transaction
        transaction.setTransactionDate(Timestamp.from(Instant.now()));

        return transaction;
    }

    /**
     * This method is used to update the savings amount
     *
     * @param oldTransaction
     * @param request
     */
    private void updateSavingsData(Transaction oldTransaction, TransactionRequest request) {
        double oldAmount = oldTransaction.getAmount();
        TransactionType oldType = oldTransaction.getType();

        SavingGoalResponse savingGoal = savingGoalService.getGoal();

        double newAmount = request.getAmount();
        TransactionType newType = request.getType();

        double totalAmount = 0;

        if (oldType.toString().equals(TransactionType.INCOME.toString())) {
            if (newType.toString().equals(TransactionType.INCOME.toString())) {
                totalAmount = (savingGoal.getCurrentSavings() - oldAmount) + newAmount;
            } else {
                totalAmount = savingGoal.getCurrentSavings() - oldAmount - newAmount;
            }
        }

        if (oldType.toString().equals(TransactionType.EXPENSE.toString())) {
            if (newType.toString().equals(TransactionType.EXPENSE.toString())) {
                totalAmount = (savingGoal.getCurrentSavings() + oldAmount) - newAmount;
            } else {
                totalAmount = savingGoal.getCurrentSavings() + oldAmount + newAmount;
            }
        }
        // Update the current savings after updating the transaction
        savingGoalService.updateCurrentSavingsForExistingTransaction(totalAmount, userService.getUsername());
    }

    /**
     * This method will return the list of transaction based on given time range
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public Page<TransactionResponse> getFilteredTransaction(String start, String end, Pageable pageable) {
        LocalDate startDate;
        LocalDate endDate;
        try {
            // Convert the given start and end string to date objects
            startDate = LocalDate.parse(start);
            endDate = LocalDate.parse(end);
        } catch (DateTimeParseException dateTimeParseException) {
            // If any issue come, throw the exception
            throw new TransactionException(UNABLE_TO_PARSE_DATE + dateTimeParseException.getMessage());
        }

        // Check if start end date is after the start date
        if (endDate.isBefore(startDate)) {
            throw new TransactionException("End date must not be before the start date");
        }

        Page<TransactionResponse> transactionResponses = getAllTransactionForUser(pageable);
        List<TransactionResponse> filteredList = transactionResponses.getContent().stream().filter(transactionResponse -> {
            LocalDate createdDate = transactionResponse.getTransactionDate().toLocalDateTime().toLocalDate();
            return (createdDate.isEqual(startDate) || createdDate.isEqual(endDate) || (createdDate.isAfter(startDate) && createdDate.isBefore(endDate)));
        }).toList();

        return new PageImpl<>(filteredList, pageable, filteredList.size());
    }

    @Override
    public Page<TransactionResponse> getTransactionsListByCategory(String category, Pageable pageable) {
        try {
            ExpenseCategory.valueOf(category);
        } catch (IllegalArgumentException exception) {
            throw new TransactionException(exception.getMessage());
        }
        Page<Transaction> transactions = transactionRepository.findTransactionByCategory(category, pageable);
        return transactionMapper.toTransactionResponseList(transactions);
    }

    @Override
    public double getTransactionAmountByCategory(String category) {
        return transactionRepository.getTotalAmountByCategory(category);
    }

}
