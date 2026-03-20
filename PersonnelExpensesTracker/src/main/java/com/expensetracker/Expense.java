package com.expensetracker;

import java.time.LocalDate;

/**
 * Expense – Core data model class for a single expense entry.
 *
 * OOP Principles demonstrated:
 *  - ENCAPSULATION : all fields are private, accessed via getters/setters
 *  - INHERITANCE   : extends BaseEntity (inherits id, createdDate)
 *  - POLYMORPHISM  : overrides toString() from BaseEntity
 *
 * Principles of Programming and Data Structures
 * Student: Saimon Shrestha | ID: 2531303
 */
public class Expense extends BaseEntity {

    // ── Private Fields (Encapsulation) ────────────────────────
    private double    amount;
    private String    category;
    private LocalDate date;
    private String    description;

    // ── Constructors ─────────────────────────────────────────

    /**
     * Constructor used when creating a NEW expense (no ID yet – DB assigns it).
     */
    public Expense(double amount, String category, LocalDate date, String description) {
        super();
        this.amount      = amount;
        this.category    = category;
        this.date        = date;
        this.description = description;
    }

    /**
     * Constructor used when LOADING an existing expense from the database.
     */
    public Expense(int id, double amount, String category, LocalDate date, String description) {
        super(id);
        this.amount      = amount;
        this.category    = category;
        this.date        = date;
        this.description = description;
    }

    // ── Getters and Setters ──────────────────────────────────

    public double    getAmount()                       { return amount; }
    public void      setAmount(double amount)          { this.amount = amount; }

    public String    getCategory()                     { return category; }
    public void      setCategory(String category)      { this.category = category; }

    public LocalDate getDate()                         { return date; }
    public void      setDate(LocalDate date)           { this.date = date; }

    public String    getDescription()                  { return description; }
    public void      setDescription(String description){ this.description = description; }

    // ── Polymorphism: override toString ──────────────────────

    /**
     * Returns a formatted string representing this expense.
     * Overrides BaseEntity.toString() – runtime polymorphism.
     */
    @Override
    public String toString() {
        return String.format("[%d] %s | %s | Rs. %.2f | %s",
                getId(), date.toString(), category, amount, description);
    }
}
