package com.example.personal_budget_planner.Controller;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.Service.Impl.SavingGoalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class BudgetController {

    @Autowired
    private SavingGoalServiceImpl savingGoalService;

    @RequestMapping(value = "/goal", method = RequestMethod.POST)
    public SavingGoalResponse addGoal(@RequestBody SavingGoalRequest request) {
        return savingGoalService.setGoal(request);
    }


    @RequestMapping(value = "/goal/{goalID}", method = RequestMethod.GET)
    public SavingGoalResponse getGoal(@PathVariable String goalID) {
        return savingGoalService.getGoal(goalID);
    }

    @RequestMapping(value = "/goal/{goalID}", method = RequestMethod.DELETE)
    public SavingGoalResponse deleteGoal(@PathVariable String goalID) {
        return savingGoalService.getGoal(goalID);
    }

}
