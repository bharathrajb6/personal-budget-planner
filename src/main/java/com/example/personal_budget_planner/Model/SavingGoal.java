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
@Table(name = "savingGoal")
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

    @Column(name = "monthlyTarget")
    private double monthlyTarget;

    @Column(name = "yearlyTarget")
    private double yearlyTarget;

    @Column(name = "currentSavings")
    private double currentSavings;
}
