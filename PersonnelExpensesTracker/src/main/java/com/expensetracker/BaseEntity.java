package com.expensetracker;

import java.time.LocalDate;

/**
 * BaseEntity – Abstract base class shared by Expense and Category.
 * Demonstrates INHERITANCE – Expense and Category both extend this class.
 *
 * Principles of Programming and Data Structures
 * Student: Saimon Shrestha | ID: 2531303
 */
public abstract class BaseEntity {

    // Private fields – ENCAPSULATION
    private int id;
    private LocalDate createdDate;

    // Default constructor
    public BaseEntity() {
        this.createdDate = LocalDate.now();
    }

    public BaseEntity(int id) {
        this.id          = id;
        this.createdDate = LocalDate.now();
    }

    // ── Getters and Setters ───────────────────────────────────
    public int getId()                   { return id; }
    public void setId(int id)            { this.id = id; }

    public LocalDate getCreatedDate()                    { return createdDate; }
    public void      setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }

    /**
     * Abstract toString – each subclass overrides this (POLYMORPHISM).
     */
    @Override
    public abstract String toString();
}
