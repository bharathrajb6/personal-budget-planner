package com.example.personal_budget_planner.Service;


import java.io.PrintWriter;

public interface CSVExportService {

    void exportTransaction(PrintWriter printWriter);

    void exportTransaction(String start, String end, PrintWriter writer);
}
