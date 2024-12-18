package com.example.personal_budget_planner.Controller;

import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.DTO.Response.UserResponse;
import com.example.personal_budget_planner.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

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
    public List<UserResponse> getAllUsers() {
        return adminService.getAllUsersData();
    }

    /**
     * Get all transactions
     *
     * @return
     */
    @RequestMapping(value = "/transaction", method = RequestMethod.GET)
    public List<TransactionResponse> getAllTransactions() {
        return adminService.getAllTransaction();
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
    public List<TransactionResponse> getAllGoalsByUsername(@PathVariable String username) {
        return adminService.getAllTransactionForUser(username);
    }

    /**
     * Get all saving goals
     *
     * @return
     */
    @RequestMapping(value = "/goal", method = RequestMethod.GET)
    public List<SavingGoalResponse> getAllGoals() {
        return adminService.getAllGoals();
    }

    /**
     * Get saving goal by goalID
     *
     * @param goalID
     * @return
     */
    @RequestMapping(value = "/goal/{goalID}", method = RequestMethod.GET)
    public SavingGoalResponse getGoalByID(@PathVariable String goalID) {
        return adminService.getGoalDetails(goalID);
    }
}
