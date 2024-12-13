package com.example.personal_budget_planner.Repository;

import com.example.personal_budget_planner.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("select t from Transaction t where t.transactionID = ?1")
    Transaction findByTransactionID(String transactionID);
}
