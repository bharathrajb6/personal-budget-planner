package com.example.personal_budget_planner.Controller;

import com.example.personal_budget_planner.DTO.Request.UserRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.DTO.Response.UserResponse;
import com.example.personal_budget_planner.Service.AdminService;
import com.example.personal_budget_planner.Service.UserService;
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
    private final UserService userService;

    /**
     * Get user data by username
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    public UserResponse getUserData(@PathVariable String username) {
        return adminService.getUserData(username);
    }

    /**
     * Get all users data
     *
     * @return
     */
    @RequestMapping(value = "user/all", method = RequestMethod.GET)
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
     * @param username
     * @return
     */
    @RequestMapping(value = "/goal/{username}", method = RequestMethod.GET)
    public SavingGoalResponse getGoalByUsername(@PathVariable String username) {
        return adminService.getGoalDetails(username);
    }

    /**
     * This method will return the user information
     *
     * @return
     */
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public UserResponse getUserDetails() {
        return userService.getUserDetails();
    }

    /**
     * This method is used to update the user details
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateDetails", method = RequestMethod.POST)
    public UserResponse updateUserDetails(@RequestBody UserRequest request) {
        return userService.updateUserDetails(request);
    }

    /**
     * This method is used to update the password
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public UserResponse updatePassword(@RequestBody UserRequest request) {
        return userService.updateUserPassword(request);
    }

}
