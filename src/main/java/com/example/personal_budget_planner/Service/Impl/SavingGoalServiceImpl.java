package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.Exceptions.GoalException;
import com.example.personal_budget_planner.Mapper.SavingGoalMapper;
import com.example.personal_budget_planner.Model.SavingGoal;
import com.example.personal_budget_planner.Model.TransactionType;
import com.example.personal_budget_planner.Repository.SavingGoalRepository;
import com.example.personal_budget_planner.Service.EmailService;
import com.example.personal_budget_planner.Service.RedisService;
import com.example.personal_budget_planner.Service.SavingGoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.example.personal_budget_planner.Messages.SavingGoal.SavingGoalExceptionMessages.*;
import static com.example.personal_budget_planner.Messages.SavingGoal.SavingGoalLogMessages.*;
import static com.example.personal_budget_planner.Validations.GoalValidation.validateGoalDetails;

@Service
@Slf4j
@RequiredArgsConstructor
public class SavingGoalServiceImpl implements SavingGoalService {

    private final SavingGoalMapper goalMapper;
    private final SavingGoalRepository savingGoalRepository;
    private final UserServiceImpl userService;
    private final RedisService redisService;
    private final EmailService emailService;

    /**
     * This method is used to save the goal
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public SavingGoalResponse setGoal(SavingGoalRequest request) {
        // Validate the goal object
        validateGoalDetails(request);

        // Convert the request object to goal object
        SavingGoal savingGoal = goalMapper.toSavingGoal(request);
        savingGoal.setGoalID(UUID.randomUUID().toString());
        savingGoal.setUsername(userService.getUsername());

        try {
            // Save the goal details to database
            savingGoalRepository.save(savingGoal);
            notifyUser(userService.getUsername());
            log.info(GOAL_SAVED_SUCCESSFULLY);
        } catch (Exception exception) {
            // If any issues come, then throw the exception
            log.error(String.format(UNABLE_TO_SAVE_THE_GOAL, exception.getMessage()));
            throw new GoalException(String.format(UNABLE_TO_SAVE_GOAL, exception.getMessage()));
        }
        return getGoal();
    }

    /**
     * This method is used to get the goal by its ID
     *
     * @return
     */
    @Override
    public SavingGoalResponse getGoal() {
        String username = userService.getUsername();
        String key = "goal" + username;
        SavingGoalResponse savingGoalResponse = redisService.getData(key, SavingGoalResponse.class);
        if (savingGoalResponse != null) {
            return savingGoalResponse;
        } else {
            // Fetch the goal details from database
            SavingGoal savingGoal = savingGoalRepository.findByUsername(username).orElseThrow(() -> {
                // If it is not present, then throw the exception
                log.error(UNABLE_TO_FETCH_THE_GOAL);
                return new GoalException(String.format(GOAL_NOT_FOUND, username));
            });
            savingGoalResponse = goalMapper.toSavingGoalResponse(savingGoal);
            redisService.setData(key, savingGoalResponse, 300L);
            return savingGoalResponse;
        }
    }

    /**
     * This method is used to update the goal by its ID
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public SavingGoalResponse updateGoal(SavingGoalRequest request) {

        // Validate the goal request
        validateGoalDetails(request);

        String username = userService.getUsername();

        // Fetch the goal details from database
        SavingGoal existingSavingGoal = savingGoalRepository.findByUsername(username).orElseThrow(() -> {
            log.error(UNABLE_TO_FETCH_THE_GOAL);
            return new GoalException(String.format(GOAL_NOT_FOUND, username));
        });

        // Convert the request object to goal model
        SavingGoal newGoal = goalMapper.toSavingGoal(request);
        if (existingSavingGoal != null) {
            try {
                // Update the goal
                savingGoalRepository.updateSavingGoal(newGoal.getMonthlyTarget(), newGoal.getYearlyTarget(), existingSavingGoal.getGoalID());
                log.info(String.format(GOAL_UPDATED_SUCCESSFULLY, username));
                redisService.deleteData("goal" + existingSavingGoal.getUsername());
                notifyUser(userService.getUsername());
                return getGoal();
            } catch (Exception exception) {
                // If any issue come, then throw the exception
                log.error(String.format(UNABLE_TO_UPDATE_THE_GOAL, username, exception.getMessage()));
                throw new GoalException(String.format(UNABLE_TO_UPDATE_GOAL, exception.getMessage()));
            }
        }
        return getGoal();
    }

    /**
     * This method is used to update the savings of the goal
     *
     * @param amount
     * @param operation
     */
    @Override
    @Transactional
    public void updateCurrentSavingsForNewTransaction(double amount, TransactionType operation) {

        // Fetch the goal details from database
        SavingGoal savingGoal = savingGoalRepository.findByUsername(userService.getUsername()).orElseThrow(() -> {
            log.error(UNABLE_TO_FETCH_THE_GOAL);
            return new GoalException(String.format(GOAL_NOT_FOUND, userService.getUsername()));
        });

        double updatedAmount = savingGoal.getCurrentSavings();

        switch (operation) {
            case INCOME:
                updatedAmount += amount;
                break;
            case EXPENSE:
                updatedAmount -= amount;
                break;
            default:
                throw new GoalException(INVALID_OPERATION);
        }

        try {
            // Update the current savings of the user
            savingGoalRepository.updateCurrentSavings(amount, savingGoal.getUsername());
            redisService.deleteData("goal" + savingGoal.getUsername());
            notifyUser(userService.getUsername());
            log.info(String.format(GOAL_UPDATED_SUCCESSFULLY, savingGoal.getGoalID()));
        } catch (Exception exception) {
            // If any issue come, then throw the exception
            log.error(String.format(UNABLE_TO_UPDATE_THE_GOAL, savingGoal.getGoalID(), exception.getMessage()));
            throw new GoalException(String.format(UNABLE_TO_UPDATE_GOAL, exception.getMessage()));
        }

    }

    /**
     * This method will save the savings amount in database
     *
     * @param amount
     * @param username
     */
    @Override
    @Transactional
    public void updateCurrentSavingsForExistingTransaction(double amount, String username) {
        // Fetch the goal details from database
        try {
            savingGoalRepository.updateCurrentSavings(amount, username);
            notifyUser(userService.getUsername());
            redisService.deleteData("goal" + username);
        } catch (Exception exception) {
            throw new GoalException("Unable to update");
        }
    }

    /**
     * This method is used to delete the goal using its ID
     *
     * @param goalID
     */
    @Override
    @Transactional
    public void deleteGoal(String goalID) {
        // Fetch the goal by using goal ID
        SavingGoal savingGoal = savingGoalRepository.findByGoalID(goalID).orElseThrow(() -> {
            log.error(UNABLE_TO_FETCH_THE_GOAL);
            return new GoalException(String.format(GOAL_NOT_FOUND, goalID));
        });
        try {
            // Delete goal in database
            savingGoalRepository.delete(savingGoal);
            redisService.deleteData("goal" + savingGoal.getUsername());
            log.info(GOAL_DELETED_SUCCESSFULLY);
        } catch (Exception exception) {
            // If any issue come, then throw the exception
            log.error(String.format(UNABLE_TO_DELETE_THE_GOAL, goalID, exception.getMessage()));
            throw new GoalException(String.format(UNABLE_TO_DELETE_GOAL, exception.getMessage()));
        }
    }

    @Override
    public boolean checkIfGoalIsPresentForUser(String username) {
        return savingGoalRepository.findByUsername(username).isPresent();
    }

    @Override
    public void notifyUser(String username) {
        SavingGoal savingGoal = savingGoalRepository.findByUsername(username).orElseThrow(() -> {
            return new GoalException(String.format(GOAL_NOT_FOUND, username));
        });

        double currentSavings = savingGoal.getCurrentSavings();
        double monthlyTarget = savingGoal.getMonthlyTarget();
        double yearlyTarget = savingGoal.getYearlyTarget();
        String email = userService.getUserDetails().getEmail();

        // Send an email notification to user when their savings is 90% of their target
        if (currentSavings >= (yearlyTarget * 0.9) && currentSavings <= yearlyTarget) {
            emailService.sendEmail(email, "Almost near to your yearly target!!!", "Your current savings is near to your yearly target. Congrats.\n\nThanks,\nPersonal Budget Planner Team");
        } else if (currentSavings >= (monthlyTarget * 0.9) && currentSavings <= monthlyTarget) {
            emailService.sendEmail(email, "Almost near to your Monthly Target!!!", "Your current savings is near to your monthly target. Congrats.\n\nThanks,\nPersonal Budget Planner Team");
        }

        //Send an email notification to user when their target is achieved
        if (currentSavings > yearlyTarget || currentSavings > monthlyTarget) {
            emailService.sendEmail(email, "Congratulations, Amazing !!", "You have achieved your target.\n\nThanks,\nPersonal Budget Planner Team");
        }
        // Send an email notification to user when their savings is zero
        if (currentSavings == 0) {
            emailService.sendEmail(email, "Your savings is 0 !!!", "Your current savings is zero.Want to save something...?\nSave now !!!\n\nThanks,\nPersonal Budget Planner Team");
        }

    }
}
