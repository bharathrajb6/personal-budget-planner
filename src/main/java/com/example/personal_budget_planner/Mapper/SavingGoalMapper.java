package com.example.personal_budget_planner.Mapper;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.Model.SavingGoal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SavingGoalMapper {

    SavingGoalResponse toResponse(SavingGoal savingGoal);

    SavingGoal toSavingGoal(SavingGoalRequest request);
}
