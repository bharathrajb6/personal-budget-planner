package com.example.personal_budget_planner.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    @Id
    @Column(name = "transactionID")
    private String transactionID;

    @Column(name = "username")
    private String username;

    @Column(name = "amount")
    private double amount;

    @Column(name = "category")
    private String category;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private TransactionType type;

    @Column(name = "date")
    private Timestamp transactionDate;
}
