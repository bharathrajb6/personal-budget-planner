package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Request.TransactionRequest;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.Exceptions.TransactionException;
import com.example.personal_budget_planner.Mapper.TransactionMapper;
import com.example.personal_budget_planner.Model.ExpenseCategory;
import com.example.personal_budget_planner.Model.IncomeCategory;
import com.example.personal_budget_planner.Model.Transaction;
import com.example.personal_budget_planner.Model.TransactionType;
import com.example.personal_budget_planner.Repository.TransactionRepository;
import com.example.personal_budget_planner.Service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.example.personal_budget_planner.Messages.Transaction.TransactionExceptionMessages.*;

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
    public TransactionResponse addTransaction(TransactionRequest request) {

        Transaction transaction = generateTransaction(request);
        try {
            transactionRepository.save(transaction);
        } catch (Exception exception) {
            throw new TransactionException(String.format(UNABLE_SAVE_TRANSACTION, exception.getMessage()));
        }

        savingGoalService.updateCurrentSavings(transaction.getAmount(), transaction.getType());
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
        try {
            Transaction transaction = transactionRepository.findByTransactionID(transactionID).orElseThrow(() -> new TransactionException(String.format(TRANSACTION_NOT_FOUND, transactionID)));
            return transactionMapper.toTransactionResponse(transaction);
        } catch (Exception exception) {
            throw new TransactionException(String.format(UNABLE_GET_TRANSACTION, exception.getMessage()));
        }
    }

    /**
     * This method will fetch all the transaction for the user
     *
     * @return
     */
    @Override
    public List<TransactionResponse> getAllTransactionForUser() {
        List<Transaction> transactions = transactionRepository.findAllTransactionForUser(userService.getUsername());
        return transactionMapper.toTransactionResponseList(transactions);
    }

    /**
     * This method will delete the transaction of the user
     *
     * @param transactionID
     */
    @Override
    public void deleteTransaction(String transactionID) {
        try {
            transactionRepository.deleteById(transactionID);
        } catch (Exception exception) {
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
    public TransactionResponse updateTransaction(String transactionID, TransactionRequest request) {
        Transaction transaction = transactionRepository.findByTransactionID(transactionID).orElseThrow(() -> new TransactionException(String.format(TRANSACTION_NOT_FOUND, transactionID)));
        if (transaction != null) {
            double newAmount = request.getAmount();
            TransactionType newType = request.getType();
            String newCategory = getCategoryFromRequest(request.getCategory(), newType);
            try {
                transactionRepository.updateTransaction(newCategory, newAmount, newType, transactionID);
                savingGoalService.updateCurrentSavings(newAmount, newType);
            } catch (Exception exception) {
                throw new TransactionException(exception.getMessage());
            }
            return getTransaction(transactionID);
        } else {
            throw new TransactionException(String.format(TRANSACTION_NOT_FOUND, transactionID));
        }
    }

    public Transaction generateTransaction(TransactionRequest request) {
        Transaction transaction = transactionMapper.toTransaction(request);

        transaction.setTransactionID(UUID.randomUUID().toString());
        transaction.setTransactionDate(Timestamp.from(Instant.now()));
        transaction.setUsername(userService.getUsername());

        String category = transaction.getCategory();
        transaction.setCategory(getCategoryFromRequest(category, request.getType()));
        return transaction;
    }

    private String getCategoryFromRequest(String category, TransactionType type) {

        if (type == TransactionType.EXPENSE) {
            ExpenseCategory expenseCategory = ExpenseCategory.valueOf(category);
            return expenseCategory.toString();
        }

        if (type == TransactionType.INCOME) {
            IncomeCategory incomeCategory = IncomeCategory.valueOf(category);
            return incomeCategory.toString();
        }
        return null;
    }
}
