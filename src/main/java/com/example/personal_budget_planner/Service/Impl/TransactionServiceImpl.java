package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Request.TransactionRequest;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.Exceptions.TransactionException;
import com.example.personal_budget_planner.Mapper.TransactionMapper;
import com.example.personal_budget_planner.Model.Transaction;
import com.example.personal_budget_planner.Repository.TransactionRepository;
import com.example.personal_budget_planner.Service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public TransactionServiceImpl(TransactionMapper transactionMapper, TransactionRepository transactionRepository) {
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
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
        return getTransaction(transaction.getTransactionID());
    }

    @Override
    public TransactionResponse getTransaction(String transactionID) {
        try {
            Transaction transaction = transactionRepository.findByTransactionID(transactionID);
            return transactionMapper.toTransactionResponse(transaction);
        } catch (Exception exception) {
            throw new TransactionException(exception.getMessage());
        }
    }

    @Override
    public void deleteTransaction(String transactionID) {
        try {
            transactionRepository.deleteById(transactionID);
        } catch (Exception exception) {
            throw new TransactionException(exception.getMessage());
        }
    }
}
