package com.example.personal_budget_planner.Service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
