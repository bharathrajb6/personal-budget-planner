package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.DTO.Response.UserResponse;
import com.example.personal_budget_planner.Exceptions.UserException;
import com.example.personal_budget_planner.Mapper.SavingGoalMapper;
import com.example.personal_budget_planner.Mapper.TransactionMapper;
import com.example.personal_budget_planner.Mapper.UserMapper;
import com.example.personal_budget_planner.Model.Role;
import com.example.personal_budget_planner.Model.Transaction;
import com.example.personal_budget_planner.Model.User;
import com.example.personal_budget_planner.Repository.SavingGoalRepository;
import com.example.personal_budget_planner.Repository.TransactionRepository;
import com.example.personal_budget_planner.Repository.UserRepository;
import com.example.personal_budget_planner.Service.AdminService;
import com.example.personal_budget_planner.Service.SavingGoalService;
import com.example.personal_budget_planner.Service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.personal_budget_planner.Messages.User.UserExceptionMessages.USER_NOT_FOUND;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final SavingGoalRepository savingGoalRepository;
    private final UserMapper userMapper;
    private final TransactionMapper transactionMapper;
    private final SavingGoalMapper goalMapper;
    private final TransactionService transactionService;
    private final SavingGoalService savingGoalService;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, TransactionRepository transactionRepository, SavingGoalRepository savingGoalRepository, UserMapper userMapper, TransactionMapper transactionMapper, SavingGoalMapper goalMapper, TransactionService transactionService, SavingGoalService savingGoalService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.savingGoalRepository = savingGoalRepository;
        this.userMapper = userMapper;
        this.transactionMapper = transactionMapper;
        this.goalMapper = goalMapper;
        this.transactionService = transactionService;
        this.savingGoalService = savingGoalService;
    }

    /**
     * This method is used to get user data by username
     *
     * @param username
     * @return
     */
    @Override
    public UserResponse getUserData(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            return new UserException(String.format(USER_NOT_FOUND, username));
        });
        return userMapper.toUserResponse(user);
    }

    /**
     * This method is used to get all users data
     *
     * @return
     */
    @Override
    public List<UserResponse> getAllUsersData() {
        List<User> userList = userRepository.getAllUsers(Role.USER);
        return userMapper.toUserResponseList(userList);
    }

    /**
     * This method is used to get transaction data by transactionID
     *
     * @param transactionID
     * @return
     */
    @Override
    public TransactionResponse getTransactionByID(String transactionID) {
        return transactionService.getTransaction(transactionID);
    }

    /**
     * This method is used to get all transaction data
     *
     * @return
     */
    @Override
    public List<TransactionResponse> getAllTransaction() {
        return transactionMapper.toTransactionResponseList(transactionRepository.findAll());
    }

    /**
     * This method is used to get all transaction data for a user
     *
     * @param username
     * @return
     */
    @Override
    public List<TransactionResponse> getAllTransactionForUser(String username) {
        List<Transaction> transactions = transactionRepository.findAllTransactionForUser(username);
        return transactionMapper.toTransactionResponseList(transactions);
    }

    /**
     * This method is used to get goal data by goalID
     *
     * @param goalID
     * @return
     */
    @Override
    public SavingGoalResponse getGoalDetails(String goalID) {
        return savingGoalService.getGoal(goalID);
    }

    /**
     * This method is used to get all goal data
     *
     * @return
     */
    @Override
    public List<SavingGoalResponse> getAllGoals() {
        return goalMapper.toSavingGoalResponseList(savingGoalRepository.findAll());
    }
}
