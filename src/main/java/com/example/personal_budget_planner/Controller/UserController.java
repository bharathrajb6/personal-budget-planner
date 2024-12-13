package com.example.personal_budget_planner.Controller;

import com.example.personal_budget_planner.DTO.Request.UserRequest;
import com.example.personal_budget_planner.DTO.Response.UserResponse;
import com.example.personal_budget_planner.Service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping(value = "/user/getUserDetails", method = RequestMethod.GET)
    public UserResponse getUserDetails() {
        return userService.getUserDetails();
    }


    @RequestMapping(value = "/user/updateUserDetails", method = RequestMethod.POST)
    public UserResponse updateUserDetails(@RequestBody UserRequest request) {
        return userService.updateUserDetails(request);
    }

    @RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
    public UserResponse updatePassword(@RequestBody UserRequest request) {
        return userService.updateUserPassword(request);
    }

}
