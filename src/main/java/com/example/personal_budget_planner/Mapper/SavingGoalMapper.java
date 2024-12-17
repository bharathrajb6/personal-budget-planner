package com.example.personal_budget_planner.Mapper;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.Model.SavingGoal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SavingGoalMapper {

    SavingGoalResponse toSavingGoalResponse(SavingGoal savingGoal);

    List<SavingGoalResponse> toSavingGoalResponseList(List<SavingGoal> savingGoalList);

    SavingGoal toSavingGoal(SavingGoalRequest request);
}
