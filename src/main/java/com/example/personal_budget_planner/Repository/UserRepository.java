package com.example.personal_budget_planner.Repository;

import com.example.personal_budget_planner.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
