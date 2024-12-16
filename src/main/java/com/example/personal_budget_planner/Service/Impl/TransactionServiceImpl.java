package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Request.TransactionRequest;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.Exceptions.TransactionException;
import com.example.personal_budget_planner.Mapper.TransactionMapper;
import com.example.personal_budget_planner.Model.Transaction;
import com.example.personal_budget_planner.Model.TransactionType;
import com.example.personal_budget_planner.Repository.TransactionRepository;
import com.example.personal_budget_planner.Service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final SavingGoalServiceImpl savingGoalService;

    @Autowired
    public TransactionServiceImpl(TransactionMapper transactionMapper, TransactionRepository transactionRepository, SavingGoalServiceImpl savingGoalService) {
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
        this.savingGoalService = savingGoalService;
    }

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public TransactionResponse addTransaction(TransactionRequest request) {
        Transaction transaction = transactionMapper.toTransaction(request);
        transaction.setTransactionID(UUID.randomUUID().toString());
        transaction.setTransactionDate(Timestamp.from(Instant.now()));
        transaction.setUsername(getUsername());
        transactionRepository.save(transaction);
        savingGoalService.updateCurrentSavings(transaction.getAmount(), transaction.getType());
        return getTransaction(transaction.getTransactionID());
    }

    @Override
    public TransactionResponse getTransaction(String transactionID) {
        try {
            Transaction transaction = transactionRepository.findByTransactionID(transactionID).orElseThrow(() -> new TransactionException("Transaction not found."));
            return transactionMapper.toTransactionResponse(transaction);
        } catch (Exception exception) {
            throw new TransactionException(exception.getMessage());
        }
    }

    @Override
    public List<TransactionResponse> getAllTransactionForUser() {
        List<Transaction> transactions = transactionRepository.findAllTransactionForUser(getUsername());
        return transactionMapper.toTransactionResponseList(transactions);
    }

    @Override
    public void deleteTransaction(String transactionID) {
        try {
            transactionRepository.deleteById(transactionID);
        } catch (Exception exception) {
            throw new TransactionException(exception.getMessage());
        }
    }

    @Override
    public TransactionResponse updateTransaction(String transactionID, TransactionRequest request) {
        Transaction transaction = transactionRepository.findByTransactionID(transactionID).orElseThrow(() -> new TransactionException("Transaction not found"));
        if (transaction != null) {
            double newAmount = request.getAmount();
            TransactionType newType = request.getType();
            String newCategory = request.getCategory();
            try {
                transactionRepository.updateTransaction(newCategory, newAmount, newType, transactionID);
                savingGoalService.updateCurrentSavings(newAmount, newType);
            } catch (Exception exception) {
                throw new TransactionException(exception.getMessage());
            }
            return getTransaction(transactionID);
        } else {
            throw new TransactionException("Transaction found out");
        }
    }
}
