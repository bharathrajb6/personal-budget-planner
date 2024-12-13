package com.example.personal_budget_planner.Mapper;

import com.example.personal_budget_planner.DTO.Request.UserRequest;
import com.example.personal_budget_planner.Model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRequest request);
}
