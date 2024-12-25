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

    /**
     * Find a saving goal by goalID
     *
     * @param goalID
     * @return
     */
    Optional<SavingGoal> findByGoalID(String goalID);

    /**
     * Find a saving goal by username
     *
     * @param username
     * @return
     */
    Optional<SavingGoal> findByUsername(String username);

    /**
     * Update a saving goal
     *
     * @param month
     * @param year
     * @param goalID
     */
    @Modifying
    @Transactional
    @Query("UPDATE SavingGoal s SET s.monthlyTarget = ?1, s.yearlyTarget = ?2 where s.goalID = ?3")
    void updateSavingGoal(double month, double year, String goalID);

    /**
     * Update current savings for new transaction
     *
     * @param amount
     * @param username
     */
    @Modifying
    @Transactional
    @Query("UPDATE SavingGoal s SET s.currentSavings = ?1 where s.username = ?2")
    void updateCurrentSavings(double amount, String username);

}
