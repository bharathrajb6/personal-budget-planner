package com.example.personal_budget_planner.Controller;

import com.example.personal_budget_planner.DTO.Request.TransactionRequest;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * This method is used to add the transaction
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    public TransactionResponse addTransaction(@RequestBody TransactionRequest request) {
        return transactionService.addTransaction(request);
    }

    /**
     * This method is used to get the transaction details by using its ID
     *
     * @param transactionID
     * @return
     */
    @RequestMapping(value = "/transaction/{transactionID}", method = RequestMethod.GET)
    public TransactionResponse getTransaction(@PathVariable String transactionID) {
        return transactionService.getTransaction(transactionID);
    }

    /**
     * This method will return all the transactions for the user
     *
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/transaction", method = RequestMethod.GET)
    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {
        return transactionService.getAllTransactionForUser(pageable);
    }

    /**
     * This method is used to update the transaction by using its ID.
     *
     * @param transactionID
     * @param request
     * @return
     */
    @RequestMapping(value = "/transaction/{transactionID}", method = RequestMethod.PUT)
    public TransactionResponse updateTransaction(@PathVariable String transactionID, @RequestBody TransactionRequest request) {
        return transactionService.updateTransaction(transactionID, request);
    }

    /**
     * This method is used to delete the transaction
     *
     * @param transactionID
     */
    @RequestMapping(value = "/transaction/{transactionID}", method = RequestMethod.DELETE)
    public void deleteTransaction(@PathVariable String transactionID) {
        transactionService.deleteTransaction(transactionID);
    }

    /**
     * This method will return the filtered transactions for the user based on given time range
     *
     * @param start
     * @param end
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/transaction/filter", method = RequestMethod.GET)
    public Page<TransactionResponse> getTransactionFilter(@RequestParam("start") String start, @RequestParam("end") String end, Pageable pageable) {
        return transactionService.getFilteredTransaction(start, end, pageable);
    }
}
