package com.expensetracker;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ReportGenerator – Generates expense summaries and reports.
 *
 * Demonstrates ABSTRACTION – complex logic is hidden behind simple
 * public methods. The caller only needs to call generateCategorySummary()
 * to receive results without knowing internal details.
 *
 * Principles of Programming and Data Structures
 * Student: Saimon Shrestha | ID: 2531303
 */
public class ReportGenerator {

    // ── Dependency: business logic layer ─────────────────────
    private final ExpenseManager expenseManager;

    // ── Constructor ──────────────────────────────────────────

    public ReportGenerator(ExpenseManager expenseManager) {
        this.expenseManager = expenseManager;
    }

    // ══════════════════════════════════════════════════════════
    //  Report Methods
    // ══════════════════════════════════════════════════════════

    /**
     * Generate a category-wise spending summary for ALL expenses.
     * @return Map of category name -> total amount spent.
     */
    public Map<String, Double> generateCategorySummary() {
        List<Expense> allExpenses = expenseManager.getAllExpenses();
        Map<String, Double> summary = new LinkedHashMap<>();
        for (Expense e : allExpenses) {
            // merge() adds to existing key or inserts new entry
            summary.merge(e.getCategory(), e.getAmount(), Double::sum);
        }
        return summary;
    }

    /**
     * Generate a category-wise spending summary for a specific month/year.
     * @param month Month number (1–12).
     * @param year  4-digit year.
     * @return Map of category name -> total amount for that month.
     */
    public Map<String, Double> generateMonthlyReport(int month, int year) {
        List<Expense> monthly = expenseManager.getExpensesByMonth(month, year);
        Map<String, Double> summary = new LinkedHashMap<>();
        for (Expense e : monthly) {
            summary.merge(e.getCategory(), e.getAmount(), Double::sum);
        }
        return summary;
    }

    /**
     * Calculate the grand total of all expenses.
     * @return Total amount as double.
     */
    public double generateTotalReport() {
        return expenseManager.getTotalExpenses();
    }

    /**
     * Format a summary map into a readable, displayable string.
     * @param summary Map of category -> total amount.
     * @return Formatted String for display in the UI.
     */
    public String formatReport(Map<String, Double> summary) {
        StringBuilder sb = new StringBuilder();
        sb.append("════════════════════════════════════\n");
        sb.append("       EXPENSE SUMMARY REPORT       \n");
        sb.append("════════════════════════════════════\n\n");

        if (summary.isEmpty()) {
            sb.append("  No expenses found.\n");
        } else {
            double total = 0;
            for (Map.Entry<String, Double> entry : summary.entrySet()) {
                sb.append(String.format("  %-18s Rs. %,.2f%n",
                        entry.getKey() + ":", entry.getValue()));
                total += entry.getValue();
            }
            sb.append("\n────────────────────────────────────\n");
            sb.append(String.format("  %-18s Rs. %,.2f%n", "GRAND TOTAL:", total));
            sb.append("════════════════════════════════════\n");
        }
        return sb.toString();
    }
}
