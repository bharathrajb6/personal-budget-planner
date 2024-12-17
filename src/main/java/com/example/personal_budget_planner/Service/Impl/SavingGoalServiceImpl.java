package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.Exceptions.GoalException;
import com.example.personal_budget_planner.Mapper.SavingGoalMapper;
import com.example.personal_budget_planner.Model.SavingGoal;
import com.example.personal_budget_planner.Model.TransactionType;
import com.example.personal_budget_planner.Repository.SavingGoalRepository;
import com.example.personal_budget_planner.Service.SavingGoalInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.example.personal_budget_planner.Messages.SavingGoal.SavingGoalExceptionMessages.*;
import static com.example.personal_budget_planner.Messages.SavingGoal.SavingGoalLogMessages.*;
import static com.example.personal_budget_planner.Validations.GoalValidation.validateGoalDetails;

@Service
@Slf4j
public class SavingGoalServiceImpl implements SavingGoalInterface {

    private final SavingGoalMapper goalMapper;
    private final SavingGoalRepository savingGoalRepository;
    private final UserServiceImpl userService;

    @Autowired
    public SavingGoalServiceImpl(SavingGoalMapper goalMapper, SavingGoalRepository savingGoalRepository, UserServiceImpl userService) {
        this.goalMapper = goalMapper;
        this.savingGoalRepository = savingGoalRepository;
        this.userService = userService;
    }

    /**
     * This method is used to save the goal
     *
     * @param request
     * @return
     */
    @Override
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
            log.info(GOAL_SAVED_SUCCESSFULLY);
        } catch (Exception exception) {
            // If any issues come, then throw the exception
            log.error(String.format(UNABLE_TO_SAVE_THE_GOAL, exception.getMessage()));
            throw new GoalException(String.format(UNABLE_TO_SAVE_GOAL, exception.getMessage()));
        }
        return getGoal(savingGoal.getGoalID());
    }

    /**
     * This method is used to get the goal by its ID
     *
     * @param username
     * @return
     */
    @Override
    public SavingGoalResponse getGoal(String username) {
        // Fetch the goal details from database
        SavingGoal savingGoal = savingGoalRepository.findByUsername(username).orElseThrow(() -> {
            // If it is not present, then throw the exception
            log.error(UNABLE_TO_FETCH_THE_GOAL);
            return new GoalException(String.format(GOAL_NOT_FOUND, username));
        });
        return goalMapper.toSavingGoalResponse(savingGoal);
    }

    /**
     * This method is used to update the goal by its ID
     *
     * @param goalID
     * @param request
     * @return
     */
    @Override
    public SavingGoalResponse updateGoal(String goalID, SavingGoalRequest request) {

        // Validate the goal request
        validateGoalDetails(request);

        // Fetch the goal details from database
        SavingGoal existingSavingGoal = savingGoalRepository.findByGoalID(goalID).orElseThrow(() -> {
            log.error(UNABLE_TO_FETCH_THE_GOAL);
            return new GoalException(String.format(GOAL_NOT_FOUND, goalID));
        });

        // Convert the request object to goal model
        SavingGoal newGoal = goalMapper.toSavingGoal(request);
        if (existingSavingGoal != null) {
            try {
                // Update the goal
                savingGoalRepository.updateSavingGoal(newGoal.getMonthlyTarget(), newGoal.getYearlyTarget(), newGoal.getCurrentSavings(), newGoal.getGoalID());
                log.info(String.format(GOAL_UPDATED_SUCCESSFULLY, goalID));
                return getGoal(existingSavingGoal.getGoalID());
            } catch (Exception exception) {
                // If any issue come, then throw the exception
                log.error(String.format(UNABLE_TO_UPDATE_THE_GOAL, goalID, exception.getMessage()));
                throw new GoalException(String.format(UNABLE_TO_UPDATE_GOAL, exception.getMessage()));
            }
        }
        return getGoal(goalID);
    }

    /**
     * This method is used to update the savings of the goal
     *
     * @param amount
     * @param operation
     */
    @Override
    public void updateCurrentSavings(double amount, TransactionType operation) {
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
            savingGoalRepository.updateSavingGoal(savingGoal.getMonthlyTarget(), savingGoal.getYearlyTarget(), updatedAmount, savingGoal.getGoalID());
            log.info(String.format(GOAL_UPDATED_SUCCESSFULLY, savingGoal.getGoalID()));
        } catch (Exception exception) {
            // If any issue come, then throw the exception
            log.error(String.format(UNABLE_TO_UPDATE_THE_GOAL, savingGoal.getGoalID(), exception.getMessage()));
            throw new GoalException(String.format(UNABLE_TO_UPDATE_GOAL, exception.getMessage()));
        }
    }

    /**
     * This method is used to delete the goal using its ID
     *
     * @param goalID
     */
    @Override
    public void deleteGoal(String goalID) {
        // Fetch the goal by using goal ID
        SavingGoal savingGoal = savingGoalRepository.findByGoalID(goalID).orElseThrow(() -> {
            log.error(UNABLE_TO_FETCH_THE_GOAL);
            return new GoalException(String.format(GOAL_NOT_FOUND, goalID));
        });
        try {
            // Delete goal in database
            savingGoalRepository.delete(savingGoal);
            log.info(GOAL_DELETED_SUCCESSFULLY);
        } catch (Exception exception) {
            // If any issue come, then throw the exception
            log.error(String.format(UNABLE_TO_DELETE_THE_GOAL, goalID, exception.getMessage()));
            throw new GoalException(String.format(UNABLE_TO_DELETE_GOAL, exception.getMessage()));
        }
    }

}
