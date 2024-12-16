package com.example.personal_budget_planner.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "saving_goal")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingGoal {

    @Id
    @Column(name = "goalID")
    private String goalID;

    @Column(name = "username")
    private String username;

    @Column(name = "monthly_target")
    private double monthlyTarget;

    @Column(name = "yearly_target")
    private double yearlyTarget;

    @Column(name = "current_savings")
    private double currentSavings;
}
