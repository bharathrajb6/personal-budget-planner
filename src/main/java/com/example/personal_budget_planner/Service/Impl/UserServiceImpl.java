package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Request.UserRequest;
import com.example.personal_budget_planner.DTO.Response.UserResponse;
import com.example.personal_budget_planner.Exceptions.UserException;
import com.example.personal_budget_planner.Mapper.UserMapper;
import com.example.personal_budget_planner.Model.User;
import com.example.personal_budget_planner.Repository.UserRepository;
import com.example.personal_budget_planner.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.personal_budget_planner.Messages.User.UserMessages.*;
import static com.example.personal_budget_planner.Validations.UserValidation.validateUserDetails;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * This method will return username of the current user
     *
     * @return
     */
    public String getUsername() {
        // Get the user data from security context
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * This method will return details of the user
     *
     * @return
     */
    @Override
    public UserResponse getUserDetails() {
        String username = getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            log.error(USER_NOT_FOUND, username);
            return new UserException(String.format(USER_NOT_FOUND, username));
        });
        return userMapper.toUserResponse(user);
    }

    /**
     * This method will update the user details in database
     *
     * @param userRequest
     * @return
     */
    @Override
    public UserResponse updateUserDetails(UserRequest userRequest) {
        if (userRequest == null) {
            log.error(USER_ENTITY_CANNOT_NULL);
            throw new UserException(USER_ENTITY_CANNOT_NULL);
        }
        validateUserDetails(userRequest);
        User user = userMapper.toUser(userRequest);
        try {
            userRepository.updateUserData(user.getFirstName(), user.getLastName(), user.getEmail(), user.getContactNumber(), user.getUsername());
            log.info(USER_DATA_UPDATED_SUCCESSFULLY, userRequest.getUsername());
        } catch (Exception exception) {
            log.error(UNABLE_TO_UPDATE_USER_DATA, userRequest.getUsername());
            throw new UserException(exception.getMessage());
        }
        return getUserDetails();
    }

    /**
     * This method will update the password
     *
     * @param userRequest
     * @return
     */
    @Override
    public UserResponse updateUserPassword(UserRequest userRequest) {
        String username = getUsername();
        String password = userRequest.getPassword();
        if (password != null && !password.isEmpty()) {
            String encodedPassword = passwordEncoder.encode(password);
            try {
                userRepository.updateUserPassword(encodedPassword, username);
                log.info(USER_PASSWORD_UPDATED_SUCCESSFULLY, username);
            } catch (Exception exception) {
                log.error(UNABLE_TO_UPDATE_USER_PASSWORD, exception.getMessage());
                throw new UserException(exception.getMessage());
            }
            return getUserDetails();
        } else {
            log.error(PASSWORD_CANNOT_NULL);
            throw new UserException(PASSWORD_CANNOT_NULL);
        }
    }
}
