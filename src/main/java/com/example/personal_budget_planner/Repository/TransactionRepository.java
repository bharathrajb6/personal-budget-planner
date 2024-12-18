package com.example.personal_budget_planner.Repository;

import com.example.personal_budget_planner.Model.Transaction;
import com.example.personal_budget_planner.Model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    /**
     * This method is used to find a transaction by its transactionID
     *
     * @param transactionID
     * @return
     */
    @Query("SELECT t from Transaction t where t.transactionID = ?1")
    Optional<Transaction> findByTransactionID(String transactionID);

    /**
     * This method is used to find all transactions for a user
     *
     * @param username
     * @return
     */
    @Query("SELECT t from Transaction t where t.username = ?1")
    List<Transaction> findAllTransactionForUser(String username);

    /**
     * This method is used to update a transaction
     *
     * @param category
     * @param amount
     * @param type
     * @param transactionID
     */
    @Modifying
    @Transactional
    @Query("UPDATE Transaction t SET t.category = ?1, t.amount = ?2, t.type = ?3 where t.transactionID = ?4")
    void updateTransaction(String category, double amount, TransactionType type, String transactionID);
}
