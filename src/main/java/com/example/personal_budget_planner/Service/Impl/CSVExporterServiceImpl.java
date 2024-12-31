package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.Mapper.TransactionMapper;
import com.example.personal_budget_planner.Model.Transaction;
import com.example.personal_budget_planner.Repository.TransactionRepository;
import com.example.personal_budget_planner.Service.CSVExportService;
import com.example.personal_budget_planner.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class CSVExporterServiceImpl implements CSVExportService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final TransactionMapper transactionMapper;

    @Override
    public void exportTransaction(PrintWriter writer) {

        List<Transaction> transactions = transactionRepository.findAllTransactionForUserForExport(userService.getUsername());
        List<TransactionResponse> transactionResponses = transactionMapper.toTransactionResponseList(transactions);

        writer.println("TransactionID,Username,Amount,Category,Type,TransactionDate");

        for (TransactionResponse transactionResponse : transactionResponses) {
            String result = String.join(",", transactionResponse.getTransactionID(), transactionResponse.getUsername(), String.valueOf(transactionResponse.getAmount()),
                    transactionResponse.getCategory(), transactionResponse.getType().toString(), transactionResponse.getTransactionDate().toString());
            writer.println(result);
        }
    }
}
