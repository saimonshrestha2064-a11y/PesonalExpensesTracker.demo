package com.expensetracker;

/**
 * Category – Represents an expense category.
 *
 * Extends BaseEntity (INHERITANCE).
 * Overrides toString() (POLYMORPHISM).
 *
 * Principles of Programming and Data Structures
 * Student: Saimon Shrestha | ID: 2531303
 */
public class Category extends BaseEntity {

    // Private field – ENCAPSULATION
    private String categoryName;

    // ── Constructors ─────────────────────────────────────────

    public Category(String categoryName) {
        super();
        this.categoryName = categoryName;
    }

    public Category(int id, String categoryName) {
        super(id);
        this.categoryName = categoryName;
    }

    // ── Getters and Setters ──────────────────────────────────

    public String getCategoryName()                       { return categoryName; }
    public void   setCategoryName(String categoryName)    { this.categoryName = categoryName; }

    // ── Polymorphism: override toString ──────────────────────

    /**
     * Returns the category name.
     * Used by JavaFX ComboBox to display readable category names.
     */
    @Override
    public String toString() {
        return categoryName;
    }
}
