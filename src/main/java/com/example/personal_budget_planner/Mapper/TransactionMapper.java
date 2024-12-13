package com.example.personal_budget_planner.Mapper;

import com.example.personal_budget_planner.DTO.Request.TransactionRequest;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.Model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction toTransaction(TransactionRequest request);

    TransactionResponse toTransactionResponse(Transaction transaction);
}
