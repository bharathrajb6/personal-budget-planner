package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Request.UserRequest;
import com.example.personal_budget_planner.DTO.Response.UserResponse;
import com.example.personal_budget_planner.Exceptions.UserException;
import com.example.personal_budget_planner.Mapper.UserMapper;
import com.example.personal_budget_planner.Model.User;
import com.example.personal_budget_planner.Repository.UserRepository;
import com.example.personal_budget_planner.Service.RedisService;
import com.example.personal_budget_planner.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.personal_budget_planner.Messages.User.UserExceptionMessages.*;
import static com.example.personal_budget_planner.Messages.User.UserLogMessages.*;
import static com.example.personal_budget_planner.Validations.UserValidation.validateUserDetails;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    /**
     * This method will return username of the current user
     *
     * @return
     */
    @Override
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
        UserResponse userResponse = redisService.getData(username, UserResponse.class);
        if (userResponse != null) {
            return userResponse;
        } else {
            // Fetch the current user details
            User user = userRepository.findByUsername(username).orElseThrow(() -> {
                // If any come, then throw the exception
                log.error(USER_DATA_NOT_FOUND, username);
                return new UserException(String.format(USER_NOT_FOUND, username));
            });
            userResponse = userMapper.toUserResponse(user);
            redisService.setData(username, userResponse, 300L);
            return userResponse;
        }
    }

    /**
     * This method will update the user details in database
     *
     * @param userRequest
     * @return
     */
    @Override
    @Transactional
    public UserResponse updateUserDetails(UserRequest userRequest) {
        // IF user request is null, then throw the exception
        if (userRequest == null) {
            log.error(USER_DATA_CANNOT_NULL);
            throw new UserException(USER_ENTITY_CANNOT_NULL);
        }

        // Validate the request object
        validateUserDetails(userRequest);

        User user = userMapper.toUser(userRequest);

        try {
            // Update the user data
            userRepository.updateUserData(user.getFirstName(), user.getLastName(), user.getEmail(), user.getContactNumber(), user.getUsername());
            redisService.deleteData(userRequest.getUsername());
            log.info(USER_DATA_UPDATED_SUCCESSFULLY, userRequest.getUsername());
        } catch (Exception exception) {
            // If any come, then throw the exception
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
    @Transactional
    public UserResponse updateUserPassword(UserRequest userRequest) {
        String username = getUsername();
        String password = userRequest.getPassword();

        if (password != null && !password.isEmpty()) {
            // Encode the password
            String encodedPassword = passwordEncoder.encode(password);
            try {
                // Update the user password
                userRepository.updateUserPassword(encodedPassword, username);
                log.info(USER_PASSWORD_UPDATED_SUCCESSFULLY, username);
            } catch (Exception exception) {
                // If any come, then throw the exception
                log.error(UNABLE_TO_UPDATE_USER_PASSWORD, exception.getMessage());
                throw new UserException(exception.getMessage());
            }
            redisService.deleteData(userRequest.getUsername());
            return getUserDetails();
        } else {
            // If any come, then throw the exception
            log.error(PASSWORD_CANNOT_NULL);
            throw new UserException(PASSWORD_CANNOT_NULL);
        }
    }
}
