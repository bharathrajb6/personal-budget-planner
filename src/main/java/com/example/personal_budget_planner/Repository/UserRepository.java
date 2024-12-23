package com.example.personal_budget_planner.Repository;

import com.example.personal_budget_planner.Model.Role;
import com.example.personal_budget_planner.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * This method is used to get the user by username
     *
     * @param username
     * @return
     */
    Optional<User> findByUsername(String username);

    /**
     * This method is used to update the user data
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param contact
     * @param username
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.firstName = ?1, u.lastName = ?2, u.email = ?3, u.contactNumber = ?4 where u.username = ?5")
    void updateUserData(String firstName, String lastName, String email, String contact, String username);


    /**
     * This method is used to update the user password
     *
     * @param password
     * @param username
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = ?1 where u.username = ?2")
    void updateUserPassword(String password, String username);

    /**
     * This method is used to get all the users by role
     *
     * @param role
     * @return
     */
    @Query("SELECT u from User u where u.role = ?1")
    Page<User> getAllUsers(Role role, Pageable pageable);
}
