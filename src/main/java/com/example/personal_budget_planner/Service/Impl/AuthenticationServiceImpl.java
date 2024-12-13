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

import static com.example.personal_budget_planner.Validations.UserValidation.validateUserDetails;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String register(UserRequest request) {
        if (userRepository.findById(request.getUsername()).isPresent()) {
            throw new UserException("User already exists");
        }
        validateUserDetails(request);
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        saveUserToken(jwtToken, user);
        return jwtToken;
    }

    @Override
    public String login(UserRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception exception) {
            authentication = null;
            log.error("UNABLE_TO_FIND_ACCOUNT", request.getUsername());
        }
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user != null && authentication != null) {
            String jwtToken = jwtService.generateToken(user);
            revokeAllTokensForUser(user.getUsername());
            saveUserToken(jwtToken, user);
            return jwtToken;
        } else {
            return null;
        }
    }

    private void saveUserToken(String jwt_token, User user) {
        Token token = new Token();
        token.setTokenId(String.valueOf(UUID.randomUUID()));
        token.setToken(jwt_token);
        token.setUser(user);
        token.setLoggedOut(false);
        tokenRepository.save(token);
        log.info("USER_TOKEN_SAVED_SUCCESSFULLY");
    }

    private void revokeAllTokensForUser(String username) {
        List<Token> tokenList = tokenRepository.findAllTokens(username);
        if (!tokenList.isEmpty()) {
            tokenList.forEach(t -> t.setLoggedOut(true));
        }
        tokenRepository.deleteAll(tokenList);
    }
}
