package com.example.personal_budget_planner.Mapper;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.DTO.Response.UserResponse;
import com.example.personal_budget_planner.Model.SavingGoal;
import com.example.personal_budget_planner.Model.User;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SavingGoalMapper {

    SavingGoalResponse toSavingGoalResponse(SavingGoal savingGoal);

    default Page<SavingGoalResponse> toSavingGoalResponseList(Page<SavingGoal> savingGoals) {
        List<SavingGoalResponse> responses = savingGoals.getContent().stream().map(this::toSavingGoalResponse).collect(Collectors.toList());
        return new PageImpl<>(responses, savingGoals.getPageable(), savingGoals.getTotalElements());
    }

    SavingGoal toSavingGoal(SavingGoalRequest request);
}
