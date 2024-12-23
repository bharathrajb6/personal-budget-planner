package com.example.personal_budget_planner.Controller;

import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.DTO.Response.UserResponse;
import com.example.personal_budget_planner.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * Get user data by username
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/getUser/{username}", method = RequestMethod.GET)
    public UserResponse getUserData(@PathVariable String username) {
        return adminService.getUserData(username);
    }

    /**
     * Get all users data
     *
     * @return
     */
    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return adminService.getAllUsersData(pageable);
    }

    /**
     * Get all transactions
     *
     * @return
     */
    @RequestMapping(value = "/transaction", method = RequestMethod.GET)
    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {
        return adminService.getAllTransaction(pageable);
    }

    /**
     * Get transaction by transactionID
     *
     * @param transactionID
     * @return
     */
    @RequestMapping(value = "/transaction/{transactionID}", method = RequestMethod.GET)
    public TransactionResponse getTransaction(@PathVariable String transactionID) {
        return adminService.getTransactionByID(transactionID);
    }

    /**
     * Get all transactions by username
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/transaction/user/{username}", method = RequestMethod.GET)
    public Page<TransactionResponse> getAllGoalsByUsername(@PathVariable String username, Pageable pageable) {
        return adminService.getAllTransactionForUser(username, pageable);
    }

    /**
     * Get all saving goals
     *
     * @return
     */
    @RequestMapping(value = "/goal", method = RequestMethod.GET)
    public Page<SavingGoalResponse> getAllGoals(Pageable pageable) {
        return adminService.getAllGoals(pageable);
    }

    /**
     * Get saving goal by goalID
     *
     * @param goalID
     * @return
     */
    @RequestMapping(value = "/goal/{username}", method = RequestMethod.GET)
    public SavingGoalResponse getGoalByUsername(@PathVariable String username) {
        return adminService.getGoalDetails(username);
    }
}
