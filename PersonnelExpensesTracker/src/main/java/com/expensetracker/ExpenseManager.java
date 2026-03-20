package com.expensetracker;

import java.util.List;

/**
 * ExpenseManager – Business Logic Layer (Controller in MVC).
 *
 * Handles all expense-related operations by coordinating between
 * the UI (MainController) and the data layer (DatabaseHelper).
 *
 * Demonstrates ABSTRACTION – the MainController calls simple methods
 * like addExpense() without knowing any SQL or database details.
 *
 * Principles of Programming and Data Structures
 * Student: Saimon Shrestha | ID: 2531303
 */
public class ExpenseManager {

    // ── Dependency: data layer ────────────────────────────────
    private final DatabaseHelper dbHelper;

    // ── Constructor ──────────────────────────────────────────

    public ExpenseManager() {
        this.dbHelper = new DatabaseHelper();
    }

    // ══════════════════════════════════════════════════════════
    //  CRUD Operations
    // ══════════════════════════════════════════════════════════

    /**
     * Validate and add a new expense to the database.
     * @param expense Expense object with all fields filled.
     * @return true if saved successfully, false if validation fails.
     */
    public boolean addExpense(Expense expense) {
        if (!validateExpense(expense)) return false;
        return dbHelper.insertExpense(expense);
    }

    /**
     * Validate and update an existing expense.
     * @param expense Expense object with updated values and valid id.
     * @return true if updated successfully.
     */
    public boolean updateExpense(Expense expense) {
        if (!validateExpense(expense)) return false;
        return dbHelper.updateExpense(expense);
    }

    /**
     * Delete an expense by its unique ID.
     * @param id The ID of the expense to delete.
     * @return true if deleted successfully.
     */
    public boolean deleteExpense(int id) {
        return dbHelper.deleteExpense(id);
    }

    /**
     * Retrieve all expenses from the database.
     * @return List of all Expense objects, ordered by date descending.
     */
    public List<Expense> getAllExpenses() {
        return dbHelper.getAllExpenses();
    }

    /**
     * Retrieve expenses filtered by category.
     * @param category Category name (e.g., "Food").
     * @return Filtered list of Expense objects.
     */
    public List<Expense> getExpensesByCategory(String category) {
        return dbHelper.getExpensesByCategory(category);
    }

    /**
     * Retrieve expenses for a specific month and year.
     * @param month Month number (1–12).
     * @param year  4-digit year.
     * @return Filtered list of Expense objects.
     */
    public List<Expense> getExpensesByMonth(int month, int year) {
        return dbHelper.getExpensesByMonth(month, year);
    }

    /**
     * Get the total amount of all recorded expenses.
     * @return Total sum as a double.
     */
    public double getTotalExpenses() {
        return dbHelper.getTotalAmount();
    }

    // ══════════════════════════════════════════════════════════
    //  Validation
    // ══════════════════════════════════════════════════════════

    /**
     * Validate an Expense object before saving.
     * Rules:
     *  - amount must be greater than 0
     *  - category must not be null or empty
     *  - date must not be null
     *  - description must not be null or empty
     *
     * @param expense The expense to validate.
     * @return true if valid, false otherwise.
     */
    public boolean validateExpense(Expense expense) {
        if (expense == null)                              return false;
        if (expense.getAmount() <= 0)                    return false;
        if (expense.getCategory() == null ||
            expense.getCategory().trim().isEmpty())       return false;
        if (expense.getDate() == null)                   return false;
        if (expense.getDescription() == null ||
            expense.getDescription().trim().isEmpty())    return false;
        return true;
    }

    // ── Close DB when app exits ──────────────────────────────

    public void close() {
        dbHelper.closeConnection();
    }
}
