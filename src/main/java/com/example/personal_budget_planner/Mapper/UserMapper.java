package com.example.personal_budget_planner.Mapper;

import com.example.personal_budget_planner.DTO.Request.UserRequest;
import com.example.personal_budget_planner.DTO.Response.TransactionResponse;
import com.example.personal_budget_planner.DTO.Response.UserResponse;
import com.example.personal_budget_planner.Model.Transaction;
import com.example.personal_budget_planner.Model.User;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRequest request);

    UserResponse toUserResponse(User user);

    default Page<UserResponse> toUserResponseList(Page<User> userPage) {
        List<UserResponse> responses = userPage.getContent()
                .stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responses, userPage.getPageable(), userPage.getTotalElements());
    }
}
