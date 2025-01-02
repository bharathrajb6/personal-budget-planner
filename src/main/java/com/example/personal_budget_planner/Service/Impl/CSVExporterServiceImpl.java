package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.Exceptions.TransactionException;
import com.example.personal_budget_planner.Mapper.TransactionMapper;
import com.example.personal_budget_planner.Model.Transaction;
import com.example.personal_budget_planner.Repository.TransactionRepository;
import com.example.personal_budget_planner.Service.CSVExportService;
import com.example.personal_budget_planner.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.example.personal_budget_planner.Messages.Transaction.TransactionExceptionMessages.UNABLE_TO_PARSE_DATE;


@Service
@Slf4j
@RequiredArgsConstructor
public class CSVExporterServiceImpl implements CSVExportService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final TransactionMapper transactionMapper;

    /**
     * This method export all the transactions to a CSV file
     *
     * @param writer
     */
    @Override
    public void exportTransaction(PrintWriter writer) {

        // Get all transactions for this user
        List<Transaction> transactions = transactionRepository.findAllTransactionForUser(userService.getUsername());

        // Convert it to response
        List<TransactionResponse> transactionResponses = transactionMapper.toTransactionResponseList(transactions);

        writer.println("TransactionID,Username,Amount,Category,Type,TransactionDate");

        for (TransactionResponse transactionResponse : transactionResponses) {
            String result = String.join(",", transactionResponse.getTransactionID(), transactionResponse.getUsername(), String.valueOf(transactionResponse.getAmount()), transactionResponse.getCategory(), transactionResponse.getType().toString(), transactionResponse.getTransactionDate().toString());
            writer.println(result);
        }
    }

    /**
     * This method will export all the transactions within specified time
     *
     * @param start
     * @param end
     * @param writer
     */
    @Override
    public void exportTransaction(String start, String end, PrintWriter writer) {

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

        // Get all transactions for this user
        List<Transaction> transactions = transactionRepository.findAllTransactionForUser(userService.getUsername());

        // Convert it to response
        List<TransactionResponse> transactionResponses = transactionMapper.toTransactionResponseList(transactions);

        // Filter the transaction based on given time
        List<TransactionResponse> filteredList = transactionResponses.stream().filter(transactionResponse -> {
            LocalDate createdDate = transactionResponse.getTransactionDate().toLocalDateTime().toLocalDate();
            return (createdDate.isEqual(startDate) || createdDate.isEqual(endDate) || (createdDate.isAfter(startDate) && createdDate.isBefore(endDate)));
        }).toList();


        writer.println("TransactionID,Username,Amount,Category,Type,TransactionDate");

        for (TransactionResponse transactionResponse : filteredList) {
            String result = String.join(",", transactionResponse.getTransactionID(), transactionResponse.getUsername(), String.valueOf(transactionResponse.getAmount()), transactionResponse.getCategory(), transactionResponse.getType().toString(), transactionResponse.getTransactionDate().toString());
            writer.println(result);
        }
    }
}
