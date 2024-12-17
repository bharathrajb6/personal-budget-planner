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

        validateGoalDetails(request);

        SavingGoal savingGoal = goalMapper.toSavingGoal(request);
        savingGoal.setGoalID(UUID.randomUUID().toString());
        savingGoal.setUsername(userService.getUsername());
        try {
            savingGoalRepository.save(savingGoal);
        } catch (Exception exception) {
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
        SavingGoal savingGoal = savingGoalRepository.findByUsername(username).orElseThrow(() -> new GoalException(String.format(GOAL_NOT_FOUND, username)));
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

        validateGoalDetails(request);
        SavingGoal existingSavingGoal = savingGoalRepository.findByGoalID(goalID).orElseThrow(() -> new GoalException(String.format(GOAL_NOT_FOUND, goalID)));

        SavingGoal newGoal = goalMapper.toSavingGoal(request);
        if (existingSavingGoal != null) {
            try {
                savingGoalRepository.updateSavingGoal(newGoal.getMonthlyTarget(), newGoal.getYearlyTarget(), newGoal.getCurrentSavings(), newGoal.getGoalID());
                return getGoal(existingSavingGoal.getGoalID());
            } catch (Exception exception) {
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
        SavingGoal savingGoal = savingGoalRepository.findByUsername(userService.getUsername()).orElseThrow(() -> new GoalException("Goal not found"));

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
            savingGoalRepository.updateSavingGoal(savingGoal.getMonthlyTarget(), savingGoal.getYearlyTarget(), updatedAmount, savingGoal.getGoalID());
        } catch (Exception exception) {
            throw new GoalException(exception.getMessage());
        }
    }

    /**
     * This method is used to delete the goal using its ID
     *
     * @param goalID
     */
    @Override
    public void deleteGoal(String goalID) {
        SavingGoal savingGoal = savingGoalRepository.findByGoalID(goalID).orElseThrow(() -> new GoalException(String.format(GOAL_NOT_FOUND, goalID)));
        try {
            savingGoalRepository.delete(savingGoal);
        } catch (Exception exception) {
            throw new GoalException(exception.getMessage());
        }
    }

}
