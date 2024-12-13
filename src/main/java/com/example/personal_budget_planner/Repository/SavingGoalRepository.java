package com.example.personal_budget_planner.Repository;

import com.example.personal_budget_planner.Model.SavingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingGoalRepository extends JpaRepository<SavingGoal, String> {


}
