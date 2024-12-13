package com.example.personal_budget_planner.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "token")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {

    @Id
    @Column(name = "token_id")
    private String tokenId;

    @Column(name = "jwt_token")
    private String token;

    @Column(name = "is_logged_out")
    private boolean isLoggedOut;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;
}
