package com.example.personal_budget_planner.Repository;

import com.example.personal_budget_planner.Model.SavingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SavingGoalRepository extends JpaRepository<SavingGoal, String> {


    SavingGoal findByGoalID(String goalID);

    @Modifying
    @Transactional
    @Query("UPDATE SavingGoal s SET s.monthlyTarget = ?1, s.yearlyTarget = ?2, s.currentSavings = ?3 where s.goalID = ?4")
    void updateSavingGoal(double month, double year, double currentSavings, String goalID);

    @Modifying
    @Transactional
    @Query("UPDATE SavingGoal s SET s.currentSavings = ?1 where s.goalID = ?2")
    void updateCurrentSavings(double current, String username);
}
