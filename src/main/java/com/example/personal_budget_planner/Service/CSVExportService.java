package com.example.personal_budget_planner.Service;


import java.io.PrintWriter;

public interface CSVExportService {

    public void exportTransaction(PrintWriter printWriter);
}
