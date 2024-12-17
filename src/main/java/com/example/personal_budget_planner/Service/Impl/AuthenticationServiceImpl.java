package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.DTO.Request.UserRequest;
import com.example.personal_budget_planner.Exceptions.UserException;
import com.example.personal_budget_planner.Mapper.UserMapper;
import com.example.personal_budget_planner.Model.Token;
import com.example.personal_budget_planner.Model.User;
import com.example.personal_budget_planner.Repository.TokenRepository;
import com.example.personal_budget_planner.Repository.UserRepository;
import com.example.personal_budget_planner.Service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.example.personal_budget_planner.Messages.User.UserExceptionMessages.*;
import static com.example.personal_budget_planner.Messages.User.UserLogMessages.*;
import static com.example.personal_budget_planner.Validations.UserValidation.validateUserDetails;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * This method is used to register the user
     *
     * @param request
     * @return
     */
    @Override
    public String register(UserRequest request) {
        // Check if username is already existed in database
        if (userRepository.findById(request.getUsername()).isPresent()) {
            // If same username is already present, then throw the exception
            log.error(String.format(USER_IS_ALREADY_EXISTS, request.getUsername()));
            throw new UserException(String.format(USER_ALREADY_EXISTS, request.getUsername()));
        }

        // Validate the user details
        validateUserDetails(request);

        // Convert the UserRequest object to User model
        User user = userMapper.toUser(request);

        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            // Save the user object to database
            userRepository.save(user);
            log.info(String.format(USER_DATA_SAVED_SUCCESSFULLY, request.getUsername()));
        } catch (Exception exception) {
            // If any issues come while saving the data, then through the exception
            log.error(String.format(UNABLE_TO_SAVE_USER_DATA_IN_DB, user.getUsername(), exception.getMessage()));
            throw new UserException(String.format(UNABLE_TO_SAVE_USER_DATA, user.getUsername(), exception.getMessage()));
        }
        // Generate new JWT token for the user
        String jwtToken = jwtService.generateToken(user);
        // Save generated token to the database along with username
        saveUserToken(jwtToken, user);
        return jwtToken;
    }

    /**
     * This method is used to handle the login
     *
     * @param request
     * @return
     */
    @Override
    public String login(UserRequest request) {
        Authentication authentication;
        try {
            // Authenticate the user with username and password
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception exception) {
            // If login credentials are wrong, then throw the exception
            authentication = null;
            log.error(USER_DATA_NOT_FOUND, request.getUsername());
            throw new UserException(String.format(USER_NOT_FOUND, request.getUsername()));
        }
        // Fetch the user details
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user != null && authentication != null) {
            // Generate a new token
            String jwtToken = jwtService.generateToken(user);
            // Delete all the existing token which is generated for this user
            revokeAllTokensForUser(user.getUsername());
            // Save the new token
            saveUserToken(jwtToken, user);
            return jwtToken;
        } else {
            return null;
        }
    }

    /**
     * This method is used to store the JWT token which is generated for the user to the database
     *
     * @param jwt_token
     * @param user
     */
    private void saveUserToken(String jwt_token, User user) {

        Token token = new Token();
        token.setTokenId(String.valueOf(UUID.randomUUID()));
        token.setToken(jwt_token);
        token.setUser(user);
        token.setLoggedOut(false);

        try {
            // Save the new token
            tokenRepository.save(token);
            log.info(JWT_TOKEN_FOR_USER_SAVED_SUCCESS);
        } catch (Exception exception) {
            // If any issue come, then throw the exception
            log.error(String.format(UNABLE_TO_SAVE_JWT_TOKEN_TO_DB, exception.getMessage()));
            throw new UserException(String.format(UNABLE_TO_SAVE_JWT_TOKEN, exception.getMessage()));
        }
    }

    /**
     * This method will delete all the existing tokens which is generated for the user
     *
     * @param username
     */
    private void revokeAllTokensForUser(String username) {
        // Fetch all the tokens for this username
        List<Token> tokenList = tokenRepository.findAllTokens(username);
        if (!tokenList.isEmpty()) {
            tokenList.forEach(t -> t.setLoggedOut(true));
        }
        try {
            // Delete all the tokens from database
            tokenRepository.deleteAll(tokenList);
            log.info(JWT_TOKEN_FOR_USER_DELETED_SUCCESS);
        } catch (Exception exception) {
            // If any issue come, then throw the exception
            String message = String.format(UNABLE_TO_DELETE_ALL_OLD_TOKENS, username, exception.getMessage());
            log.error(message);
            throw new UserException(message);
        }
    }
}
