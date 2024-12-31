package com.example.personal_budget_planner.Mapper;

import com.example.personal_budget_planner.DTO.Request.TransactionRequest;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.Model.Transaction;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    Transaction toTransaction(TransactionRequest request);

    TransactionResponse toTransactionResponse(Transaction transaction);

    // Custom method to map Page<Transaction> to Page<TransactionResponse>
    default Page<TransactionResponse> toTransactionResponseList(Page<Transaction> transactionPage) {
        List<TransactionResponse> responses = transactionPage.getContent()
                .stream()
                .map(this::toTransactionResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responses, transactionPage.getPageable(), transactionPage.getTotalElements());
    }

    List<TransactionResponse> toTransactionResponseList(List<Transaction> transactions);
}
