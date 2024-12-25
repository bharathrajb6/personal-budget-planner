package com.example.personal_budget_planner.Controller;

import com.example.personal_budget_planner.DTO.Request.SavingGoalRequest;
import com.example.personal_budget_planner.DTO.Response.SavingGoalResponse;
import com.example.personal_budget_planner.Service.Impl.SavingGoalServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SavingGoalController {

    private final SavingGoalServiceImpl savingGoalService;

    /**
     * This method is used to add the savings goal
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/goal", method = RequestMethod.POST)
    public SavingGoalResponse addGoal(@RequestBody SavingGoalRequest request) {
        return savingGoalService.setGoal(request);
    }

    /**
     * This method is used to get the goal details by using its id
     *
     * @return
     */
    @RequestMapping(value = "/goal", method = RequestMethod.GET)
    public SavingGoalResponse getGoal() {
        return savingGoalService.getGoal();
    }

    /**
     * This method is used to update the goal by using its id
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/goal", method = RequestMethod.PUT)
    public SavingGoalResponse updateGoal(@RequestBody SavingGoalRequest request) {
        return savingGoalService.updateGoal(request);
    }

    /**
     * This method is used to delete the goal by using its id
     *
     * @param goalID
     * @return
     */
    @RequestMapping(value = "/goal/{goalID}", method = RequestMethod.DELETE)
    public void deleteGoal(@PathVariable String goalID) {
        savingGoalService.deleteGoal(goalID);
    }
}
