package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Request.TransactionRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.Exceptions.TransactionException;
import com.example.personal_budget_planner.Mapper.TransactionMapper;
import com.example.personal_budget_planner.Model.Transaction;
import com.example.personal_budget_planner.Model.TransactionType;
import com.example.personal_budget_planner.Repository.TransactionRepository;
import com.example.personal_budget_planner.Service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final SavingGoalServiceImpl savingGoalService;
    private final UserServiceImpl userService;

    @Autowired
    public TransactionServiceImpl(TransactionMapper transactionMapper, TransactionRepository transactionRepository, SavingGoalServiceImpl savingGoalService, UserServiceImpl userService) {
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
        this.savingGoalService = savingGoalService;
        this.userService = userService;
    }

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
        // Fetch the transaction details from database
        Transaction transaction = transactionRepository.findByTransactionID(transactionID).orElseThrow(() -> {
            // If any issue come, then throw the exception
            log.error(String.format(UNABLE_TO_FIND_TRANSACTION, transactionID));
            return new TransactionException(String.format(TRANSACTION_NOT_FOUND, transactionID));
        });
        return transactionMapper.toTransactionResponse(transaction);
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

        SavingGoalResponse savingGoal = savingGoalService.getGoal(userService.getUsername());

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
}
