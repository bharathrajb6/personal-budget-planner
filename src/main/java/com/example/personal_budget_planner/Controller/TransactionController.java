package com.example.personal_budget_planner.Controller;

import com.example.personal_budget_planner.DTO.Request.TransactionRequest;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    public TransactionResponse addTransaction(@RequestBody TransactionRequest request) {
        return transactionService.addTransaction(request);
    }

    @RequestMapping(value = "/transaction/{transactionID}", method = RequestMethod.GET)
    public TransactionResponse getTransaction(@PathVariable String transactionID) {
        return transactionService.getTransaction(transactionID);
    }

    @RequestMapping(value = "/transaction/{transactionID}", method = RequestMethod.DELETE)
    public void deleteTransaction(@PathVariable String transactionID) {
        transactionService.deleteTransaction(transactionID);
    }
}
