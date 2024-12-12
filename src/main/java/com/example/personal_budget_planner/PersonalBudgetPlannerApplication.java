package com.example.personal_budget_planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PersonalBudgetPlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonalBudgetPlannerApplication.class, args);
	}

}
