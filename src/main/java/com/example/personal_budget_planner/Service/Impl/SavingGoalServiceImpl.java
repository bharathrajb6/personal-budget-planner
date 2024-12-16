package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.Exceptions.BudgetException;
import com.example.personal_budget_planner.Mapper.SavingGoalMapper;
import com.example.personal_budget_planner.Model.SavingGoal;
import com.example.personal_budget_planner.Model.TransactionType;
import com.example.personal_budget_planner.Repository.SavingGoalRepository;
import com.example.personal_budget_planner.Service.SavingGoalInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class SavingGoalServiceImpl implements SavingGoalInterface {

    private final SavingGoalMapper goalMapper;
    private final SavingGoalRepository savingGoalRepository;

    @Autowired
    public SavingGoalServiceImpl(SavingGoalMapper goalMapper, SavingGoalRepository savingGoalRepository) {
        this.goalMapper = goalMapper;
        this.savingGoalRepository = savingGoalRepository;
    }

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public SavingGoalResponse setGoal(SavingGoalRequest request) {
        SavingGoal savingGoal = goalMapper.toSavingGoal(request);
        savingGoal.setGoalID(UUID.randomUUID().toString());
        savingGoal.setUsername(getUsername());
        try {
            savingGoalRepository.save(savingGoal);
        } catch (Exception exception) {
            throw new BudgetException(exception.getMessage());
        }
        return getGoal(savingGoal.getGoalID());
    }

    @Override
    public SavingGoalResponse getGoal(String username) {
        SavingGoal savingGoal = savingGoalRepository.findByUsername(username).orElseThrow(() -> {
            return new BudgetException("Goal not found");
        });
        return goalMapper.toSavingGoalResponse(savingGoal);
    }

    @Override
    public SavingGoalResponse updateGoal(String goalID, SavingGoalRequest request) {
        SavingGoal existingSavingGoal = savingGoalRepository.findByGoalID(goalID).orElseThrow(() -> {
            return new BudgetException("Goal Not found");
        });

        SavingGoal newGoal = goalMapper.toSavingGoal(request);
        if (existingSavingGoal != null) {
            try {
                savingGoalRepository.updateSavingGoal(newGoal.getMonthlyTarget(), newGoal.getYearlyTarget(), newGoal.getCurrentSavings(), newGoal.getGoalID());
                return getGoal(existingSavingGoal.getGoalID());
            } catch (Exception exception) {
                throw new BudgetException(exception.getMessage());
            }
        }
        return getGoal(goalID);
    }

    @Override
    public void updateCurrentSavings(double amount, TransactionType operation) {
        SavingGoal savingGoal = savingGoalRepository.findByUsername(getUsername()).orElseThrow(() -> new BudgetException("Goal not found"));

        double updatedAmount = savingGoal.getCurrentSavings();

        switch (operation) {
            case INCOME:
                updatedAmount += amount;
                break;
            case EXPENSE:
                updatedAmount -= amount;
                break;
            default:
                throw new BudgetException("Invalid operation");
        }
        try {
            savingGoalRepository.updateSavingGoal(savingGoal.getMonthlyTarget(), savingGoal.getYearlyTarget(), updatedAmount, savingGoal.getGoalID());
        } catch (Exception exception) {
            throw new BudgetException(exception.getMessage());
        }
    }

}
