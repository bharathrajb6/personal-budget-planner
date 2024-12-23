package com.example.personal_budget_planner.Controller;

import com.example.personal_budget_planner.DTO.Request.UserRequest;
import com.example.personal_budget_planner.DTO.Response.UserResponse;
import com.example.personal_budget_planner.Service.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    /**
     * This method will return the user information
     *
     * @return
     */
    @RequestMapping(value = "/user/getUserDetails", method = RequestMethod.GET)
    public UserResponse getUserDetails() {
        return userService.getUserDetails();
    }

    /**
     * This method is used to update the user details
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/updateUserDetails", method = RequestMethod.POST)
    public UserResponse updateUserDetails(@RequestBody UserRequest request) {
        return userService.updateUserDetails(request);
    }

    /**
     * This method is used to update the password
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
    public UserResponse updatePassword(@RequestBody UserRequest request) {
        return userService.updateUserPassword(request);
    }

}
