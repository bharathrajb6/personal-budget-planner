package com.example.personal_budget_planner.Repository;

import com.example.personal_budget_planner.Model.SavingGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SavingGoalRepository extends JpaRepository<SavingGoal, String> {
    Optional<SavingGoal> findByGoalID(String goalID);

    Optional<SavingGoal> findByUsername(String username);

    @Modifying
    @Transactional
    @Query("UPDATE SavingGoal s SET s.monthlyTarget = ?1, s.yearlyTarget = ?2, s.currentSavings = ?3 where s.goalID = ?4")
    void updateSavingGoal(double month, double year, double currentSavings, String goalID);

}
