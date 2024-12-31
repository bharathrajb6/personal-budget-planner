package com.example.personal_budget_planner.Controller;

import com.example.personal_budget_planner.DTO.Request.TransactionRequest;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.Service.CSVExportService;
import com.example.personal_budget_planner.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;


@RestController
@CrossOrigin
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final CSVExportService csvExportService;

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

    @RequestMapping(value = "/transaction/category/{value}", method = RequestMethod.GET)
    public Page<TransactionResponse> getTransactionByCategory(@PathVariable String value, Pageable pageable) {
        return transactionService.getTransactionsListByCategory(value, pageable);
    }

    @RequestMapping(value = "/transaction/category/{value}/total", method = RequestMethod.GET)
    public double getTransactionByCategory(@PathVariable String value) {
        return transactionService.getTransactionAmountByCategory(value);
    }

    @RequestMapping(value = "/transaction/export", method = RequestMethod.GET)
    public ResponseEntity<byte[]> exportTransaction() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(outputStream);

            // Write the data to file
            csvExportService.exportTransaction(writer);
            writer.flush();

            // Set headers for download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv");
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
