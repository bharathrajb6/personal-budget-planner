package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.Mapper.BudgetMapper;
import com.example.personal_budget_planner.Model.SavingGoal;
import com.example.personal_budget_planner.Repository.SavingGoalRepository;
import com.example.personal_budget_planner.Service.BudgetService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.ap.shaded.freemarker.core.BugException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class SavingGoalServiceImpl implements BudgetService {

    @Autowired
    private BudgetMapper budgetMapper;

    @Autowired
    private SavingGoalRepository savingGoalRepository;

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public SavingGoalResponse setGoal(SavingGoalRequest request) {
        SavingGoal savingGoal = budgetMapper.toSavingGoal(request);
        savingGoal.setGoalID(UUID.randomUUID().toString());
        savingGoal.setUsername(getUsername());
        try {
            savingGoalRepository.save(savingGoal);
        } catch (Exception exception) {
            throw new BugException(exception.getMessage());
        }
        return getGoal(savingGoal.getGoalID());
    }

    @Override
    public SavingGoalResponse getGoal(String goalID) {
        SavingGoal savingGoal = savingGoalRepository.findByGoalID(goalID);
        return budgetMapper.toResponse(savingGoal);
    }
}
