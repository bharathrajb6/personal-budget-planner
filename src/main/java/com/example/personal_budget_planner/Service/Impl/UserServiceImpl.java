package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Request.UserRequest;
import com.example.personal_budget_planner.DTO.Response.UserResponse;
import com.example.personal_budget_planner.Exceptions.UserException;
import com.example.personal_budget_planner.Mapper.UserMapper;
import com.example.personal_budget_planner.Model.User;
import com.example.personal_budget_planner.Repository.UserRepository;
import com.example.personal_budget_planner.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.personal_budget_planner.Validations.UserValidation.validateUserDetails;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public UserResponse getUserDetails() {
        String username = getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserException("User not found"));
        return userMapper.toUserResponse(user);
    }


    @Override
    public UserResponse updateUserDetails(UserRequest userRequest) {
        if (userRequest == null) {
            throw new UserException("User data cannot be null");
        }
        validateUserDetails(userRequest);
        User user = userMapper.toUser(userRequest);
        try {
            userRepository.updateUserData(user.getFirstName(), user.getLastName(), user.getEmail(), user.getContactNumber(), user.getUsername());
        } catch (Exception exception) {
            throw new UserException(exception.getMessage());
        }
        return getUserDetails();
    }


    @Override
    public UserResponse updateUserPassword(UserRequest userRequest) {
        String username = getUsername();
        String password = userRequest.getPassword();
        if (password != null && !password.isEmpty()) {
            String encodedPassword = passwordEncoder.encode(password);
            try {
                userRepository.updateUserPassword(encodedPassword, username);
            } catch (Exception exception) {
                throw new UserException(exception.getMessage());
            }
            return getUserDetails();
        } else {
            throw new UserException("Password cannot be null");
        }
    }
}
