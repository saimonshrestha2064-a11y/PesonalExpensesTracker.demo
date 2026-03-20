package com.expensetracker;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHelper – Manages all SQLite database operations.
 *
 * Responsibilities:
 *  - Create and initialise the database and tables on first run
 *  - Provide CRUD methods for the Expense table
 *
 * The database file (expenses.db) is created automatically in the
 * project root directory when the application first starts.
 *
 * Principles of Programming and Data Structures
 * Student: Saimon Shrestha | ID: 2531303
 */
public class DatabaseHelper {

    // SQLite database file – stored in the user's home directory
    private static final String DB_URL = "jdbc:sqlite:expenses.db";

    // ── Singleton connection ─────────────────────────────────
    private Connection connection;

    // ── Constructor ──────────────────────────────────────────

    public DatabaseHelper() {
        connect();
        createTables();
    }

    // ── Connect to database ──────────────────────────────────

    private void connect() {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to SQLite database successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }

    // ── Create tables if they don't exist ────────────────────

    private void createTables() {
        String createExpenseTable =
            "CREATE TABLE IF NOT EXISTS expenses (" +
            "  id          INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  amount      REAL    NOT NULL," +
            "  category    TEXT    NOT NULL," +
            "  date        TEXT    NOT NULL," +
            "  description TEXT," +
            "  created_date TEXT" +
            ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createExpenseTable);
            System.out.println("Database tables initialised.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════
    //  CRUD OPERATIONS
    // ══════════════════════════════════════════════════════════

    /**
     * INSERT a new expense record into the database.
     * @param expense The Expense object to save.
     * @return true if successful, false otherwise.
     */
    public boolean insertExpense(Expense expense) {
        String sql = "INSERT INTO expenses (amount, category, date, description, created_date) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, expense.getAmount());
            pstmt.setString(2, expense.getCategory());
            pstmt.setString(3, expense.getDate().toString());
            pstmt.setString(4, expense.getDescription());
            pstmt.setString(5, LocalDate.now().toString());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error inserting expense: " + e.getMessage());
            return false;
        }
    }

    /**
     * UPDATE an existing expense record.
     * @param expense The Expense object with updated values (must have valid id).
     * @return true if successful, false otherwise.
     */
    public boolean updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET amount=?, category=?, date=?, description=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, expense.getAmount());
            pstmt.setString(2, expense.getCategory());
            pstmt.setString(3, expense.getDate().toString());
            pstmt.setString(4, expense.getDescription());
            pstmt.setInt   (5, expense.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating expense: " + e.getMessage());
            return false;
        }
    }

    /**
     * DELETE an expense record by its ID.
     * @param id The unique ID of the expense to delete.
     * @return true if successful, false otherwise.
     */
    public boolean deleteExpense(int id) {
        String sql = "DELETE FROM expenses WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error deleting expense: " + e.getMessage());
            return false;
        }
    }

    /**
     * SELECT all expense records from the database.
     * @return A list of all Expense objects.
     */
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses ORDER BY date DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Expense e = new Expense(
                    rs.getInt   ("id"),
                    rs.getDouble("amount"),
                    rs.getString("category"),
                    LocalDate.parse(rs.getString("date")),
                    rs.getString("description")
                );
                expenses.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching expenses: " + e.getMessage());
        }
        return expenses;
    }

    /**
     * SELECT expenses filtered by category.
     * @param category Category name to filter by.
     * @return Filtered list of Expense objects.
     */
    public List<Expense> getExpensesByCategory(String category) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE category=? ORDER BY date DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Expense e = new Expense(
                    rs.getInt   ("id"),
                    rs.getDouble("amount"),
                    rs.getString("category"),
                    LocalDate.parse(rs.getString("date")),
                    rs.getString("description")
                );
                expenses.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Error filtering expenses: " + e.getMessage());
        }
        return expenses;
    }

    /**
     * SELECT expenses for a specific month and year.
     * @param month Month number (1–12).
     * @param year  4-digit year.
     * @return Filtered list of Expense objects.
     */
    public List<Expense> getExpensesByMonth(int month, int year) {
        List<Expense> expenses = new ArrayList<>();
        String prefix = String.format("%04d-%02d", year, month);
        String sql = "SELECT * FROM expenses WHERE date LIKE ? ORDER BY date DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, prefix + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Expense e = new Expense(
                    rs.getInt   ("id"),
                    rs.getDouble("amount"),
                    rs.getString("category"),
                    LocalDate.parse(rs.getString("date")),
                    rs.getString("description")
                );
                expenses.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching monthly expenses: " + e.getMessage());
        }
        return expenses;
    }

    /**
     * Calculate the total of all expenses.
     * @return Total amount as a double.
     */
    public double getTotalAmount() {
        String sql = "SELECT SUM(amount) AS total FROM expenses";
        try (Statement stmt = connection.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            System.err.println("Error calculating total: " + e.getMessage());
        }
        return 0.0;
    }

    // ── Close connection ─────────────────────────────────────

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
