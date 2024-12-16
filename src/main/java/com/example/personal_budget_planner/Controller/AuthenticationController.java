package com.example.personal_budget_planner.Controller;

import com.example.personal_budget_planner.DTO.Request.UserRequest;
import com.example.personal_budget_planner.Service.Impl.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;

    /**
     * Adding Authentication Service dependency
     *
     * @param authenticationService
     */
    @Autowired
    public AuthenticationController(AuthenticationServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
    }

    /***
     * Register a new user
     * @param request
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody UserRequest request) {
        return authenticationService.register(request);
    }

    /***
     * Login a user
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody UserRequest request) {
        return authenticationService.login(request);
    }
}
