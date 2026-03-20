package com.expensetracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * MainController – JavaFX Controller (View layer in MVC).
 *
 * Handles all user interaction events (button clicks, form inputs,
 * table selections) and delegates work to ExpenseManager and
 * ReportGenerator.
 *
 * Principles of Programming and Data Structures
 * Student: Saimon Shrestha | ID: 2531303
 */
public class MainController implements Initializable {

    // ── FXML UI Components – Form ────────────────────────────
    @FXML private TextField       amountField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private DatePicker      datePicker;
    @FXML private TextField       descriptionField;
    @FXML private Button          addButton;
    @FXML private Button          updateButton;
    @FXML private Button          clearButton;

    // ── FXML UI Components – Table ───────────────────────────
    @FXML private TableView<Expense>            expenseTable;
    @FXML private TableColumn<Expense, Integer> colId;
    @FXML private TableColumn<Expense, String>  colDate;
    @FXML private TableColumn<Expense, String>  colCategory;
    @FXML private TableColumn<Expense, Double>  colAmount;
    @FXML private TableColumn<Expense, String>  colDescription;

    // ── FXML UI Components – Filter & Report ─────────────────
    @FXML private ComboBox<String> filterCategoryComboBox;
    @FXML private ComboBox<String> filterMonthComboBox;
    @FXML private ComboBox<String> filterYearComboBox;
    @FXML private TextArea         reportTextArea;
    @FXML private Label            totalLabel;
    @FXML private Label            statusLabel;

    // ── Business Logic ────────────────────────────────────────
    private ExpenseManager  expenseManager;
    private ReportGenerator reportGenerator;

    // ── State: tracks which expense is being edited ──────────
    private int     currentEditingId = -1;
    private boolean isEditing        = false;

    // ── Category list ────────────────────────────────────────
    private static final String[] CATEGORIES = {
        "Food", "Transport", "Bills", "Education",
        "Entertainment", "Rent", "Health", "Others"
    };

    // ════════════════════════════════════════════════════════
    //  JavaFX Lifecycle: initialize()
    // ════════════════════════════════════════════════════════

    /**
     * Called automatically by JavaFX when the FXML is loaded.
     * Sets up the table columns, combo boxes, and loads initial data.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialise business logic objects
        expenseManager  = new ExpenseManager();
        reportGenerator = new ReportGenerator(expenseManager);

        setupTableColumns();
        setupComboBoxes();

        // Set default date to today
        datePicker.setValue(LocalDate.now());

        // Set update button invisible at start
        updateButton.setVisible(false);

        // Load all expenses into the table
        loadAllExpenses();

        // Listen for table row selection → populate form for editing
        expenseTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) populateFormForEdit(newVal);
            }
        );

        setStatus("Application started. " + expenseManager.getAllExpenses().size() + " records loaded.");
    }

    // ════════════════════════════════════════════════════════
    //  Setup Helpers
    // ════════════════════════════════════════════════════════

    /** Binds the TableView columns to Expense properties. */
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Custom cell for amount – format as Rs. X,XXX.00
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colAmount.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                setText(empty || amount == null ? null
                        : String.format("Rs. %,.2f", amount));
            }
        });

        // Set column widths
        colId.setPrefWidth(50);
        colDate.setPrefWidth(100);
        colCategory.setPrefWidth(120);
        colAmount.setPrefWidth(110);
        colDescription.setPrefWidth(260);
    }

    /** Populates all ComboBox controls with appropriate values. */
    private void setupComboBoxes() {
        // Form category combo
        categoryComboBox.getItems().addAll(CATEGORIES);
        categoryComboBox.setPromptText("Select Category");

        // Filter category combo (includes "All Categories" option)
        filterCategoryComboBox.getItems().add("All Categories");
        filterCategoryComboBox.getItems().addAll(CATEGORIES);
        filterCategoryComboBox.setValue("All Categories");

        // Filter month combo
        filterMonthComboBox.getItems().addAll(
            "All Months","January","February","March","April",
            "May","June","July","August","September",
            "October","November","December"
        );
        filterMonthComboBox.setValue("All Months");

        // Filter year combo – last 5 years
        int currentYear = LocalDate.now().getYear();
        filterYearComboBox.getItems().add("All Years");
        for (int y = currentYear; y >= currentYear - 4; y--) {
            filterYearComboBox.getItems().add(String.valueOf(y));
        }
        filterYearComboBox.setValue(String.valueOf(currentYear));
    }

    // ════════════════════════════════════════════════════════
    //  FXML Button Handlers
    // ════════════════════════════════════════════════════════

    /**
     * Handles the ADD button click.
     * Reads form input, validates, creates Expense, saves to DB.
     */
    @FXML
    private void handleAddExpense() {
        // Read form fields
        String amountText = amountField.getText().trim();
        String category   = categoryComboBox.getValue();
        LocalDate date    = datePicker.getValue();
        String desc       = descriptionField.getText().trim();

        // Basic UI validation before passing to manager
        if (amountText.isEmpty() || category == null || date == null || desc.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "All fields are required. Please fill in Amount, Category, Date, and Description.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error",
                    "Amount must be a positive number (e.g., 250.00).");
            return;
        }

        // Create and save the expense
        Expense expense = new Expense(amount, category, date, desc);
        if (expenseManager.addExpense(expense)) {
            setStatus("Expense added successfully: " + category + " – Rs. " + String.format("%.2f", amount));
            clearForm();
            loadAllExpenses();
            updateTotalLabel();
        } else {
            showAlert(Alert.AlertType.ERROR, "Save Error", "Failed to save expense. Please try again.");
        }
    }

    /**
     * Handles the UPDATE button click (visible only when editing).
     * Updates the currently selected expense record.
     */
    @FXML
    private void handleUpdateExpense() {
        if (!isEditing || currentEditingId == -1) return;

        String amountText = amountField.getText().trim();
        String category   = categoryComboBox.getValue();
        LocalDate date    = datePicker.getValue();
        String desc       = descriptionField.getText().trim();

        if (amountText.isEmpty() || category == null || date == null || desc.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Amount must be a positive number.");
            return;
        }

        Expense updated = new Expense(currentEditingId, amount, category, date, desc);
        if (expenseManager.updateExpense(updated)) {
            setStatus("Expense updated successfully (ID: " + currentEditingId + ").");
            clearForm();
            loadAllExpenses();
            updateTotalLabel();
        } else {
            showAlert(Alert.AlertType.ERROR, "Update Error", "Failed to update expense.");
        }
    }

    /**
     * Handles the DELETE button click.
     * Shows confirmation dialog before deleting selected expense.
     */
    @FXML
    private void handleDeleteExpense() {
        Expense selected = expenseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select an expense row from the table to delete.");
            return;
        }

        // Confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Expense");
        confirm.setContentText("Are you sure you want to delete this expense?\n\n" + selected.toString());
        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (expenseManager.deleteExpense(selected.getId())) {
                setStatus("Expense deleted (ID: " + selected.getId() + ").");
                clearForm();
                loadAllExpenses();
                updateTotalLabel();
            } else {
                showAlert(Alert.AlertType.ERROR, "Delete Error", "Failed to delete expense.");
            }
        }
    }

    /**
     * Handles the CLEAR button – resets the form fields.
     */
    @FXML
    private void handleClearForm() {
        clearForm();
        expenseTable.getSelectionModel().clearSelection();
        setStatus("Form cleared.");
    }

    /**
     * Handles the VIEW ALL button – loads all expenses into the table.
     */
    @FXML
    private void handleViewAll() {
        filterCategoryComboBox.setValue("All Categories");
        filterMonthComboBox.setValue("All Months");
        loadAllExpenses();
        setStatus("Showing all expenses.");
    }

    /**
     * Handles the FILTER button – filters the table by category and/or month/year.
     */
    @FXML
    private void handleFilter() {
        String catFilter   = filterCategoryComboBox.getValue();
        String monthFilter = filterMonthComboBox.getValue();
        String yearFilter  = filterYearComboBox.getValue();

        List<Expense> expenses;

        // Determine which filter to apply
        boolean filterByMonth = !"All Months".equals(monthFilter) && !"All Years".equals(yearFilter);
        boolean filterByCat   = catFilter != null && !"All Categories".equals(catFilter);

        if (filterByMonth) {
            int month = getMonthNumber(monthFilter);
            int year  = Integer.parseInt(yearFilter);
            expenses  = expenseManager.getExpensesByMonth(month, year);
            if (filterByCat) {
                String finalCat = catFilter;
                expenses = expenses.stream()
                        .filter(e -> e.getCategory().equals(finalCat))
                        .toList();
            }
            setStatus("Showing " + monthFilter + " " + year +
                    (filterByCat ? " | Category: " + catFilter : "") + ".");
        } else if (filterByCat) {
            expenses = expenseManager.getExpensesByCategory(catFilter);
            setStatus("Showing category: " + catFilter + ".");
        } else {
            expenses = expenseManager.getAllExpenses();
            setStatus("Showing all expenses.");
        }

        populateTable(expenses);
        updateTotalLabel();
    }

    /**
     * Handles the GENERATE REPORT button.
     * Shows a full category summary in the report text area.
     */
    @FXML
    private void handleGenerateReport() {
        String monthFilter = filterMonthComboBox.getValue();
        String yearFilter  = filterYearComboBox.getValue();

        Map<String, Double> summary;

        if (!"All Months".equals(monthFilter) && !"All Years".equals(yearFilter)) {
            int month = getMonthNumber(monthFilter);
            int year  = Integer.parseInt(yearFilter);
            summary   = reportGenerator.generateMonthlyReport(month, year);
        } else {
            summary = reportGenerator.generateCategorySummary();
        }

        String report = reportGenerator.formatReport(summary);
        reportTextArea.setText(report);
        setStatus("Report generated.");
    }

    // ════════════════════════════════════════════════════════
    //  Helper Methods
    // ════════════════════════════════════════════════════════

    /** Load all expenses from DB and populate the table. */
    private void loadAllExpenses() {
        List<Expense> all = expenseManager.getAllExpenses();
        populateTable(all);
        updateTotalLabel();
    }

    /** Fill the TableView with a list of expenses. */
    private void populateTable(List<Expense> expenses) {
        ObservableList<Expense> data = FXCollections.observableArrayList(expenses);
        expenseTable.setItems(data);
    }

    /** Fill the form fields with data from a selected expense (for editing). */
    private void populateFormForEdit(Expense expense) {
        amountField.setText(String.valueOf(expense.getAmount()));
        categoryComboBox.setValue(expense.getCategory());
        datePicker.setValue(expense.getDate());
        descriptionField.setText(expense.getDescription());
        currentEditingId = expense.getId();
        isEditing        = true;
        addButton.setVisible(false);
        updateButton.setVisible(true);
        setStatus("Editing expense ID: " + expense.getId() + ". Modify fields and click Update.");
    }

    /** Clear all form fields and reset editing state. */
    private void clearForm() {
        amountField.clear();
        categoryComboBox.setValue(null);
        datePicker.setValue(LocalDate.now());
        descriptionField.clear();
        currentEditingId = -1;
        isEditing        = false;
        addButton.setVisible(true);
        updateButton.setVisible(false);
    }

    /** Update the total amount label at the bottom. */
    private void updateTotalLabel() {
        double total = 0;
        for (Expense e : expenseTable.getItems()) total += e.getAmount();
        totalLabel.setText(String.format("Total Shown:  Rs. %,.2f", total));
    }

    /** Set the status bar text. */
    private void setStatus(String message) {
        statusLabel.setText("  " + message);
    }

    /** Show an alert dialog. */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /** Convert month name string to number (1–12). */
    private int getMonthNumber(String monthName) {
        return switch (monthName) {
            case "January"   -> 1;
            case "February"  -> 2;
            case "March"     -> 3;
            case "April"     -> 4;
            case "May"       -> 5;
            case "June"      -> 6;
            case "July"      -> 7;
            case "August"    -> 8;
            case "September" -> 9;
            case "October"   -> 10;
            case "November"  -> 11;
            case "December"  -> 12;
            default          -> LocalDate.now().getMonthValue();
        };
    }

    /** Called when the application window is closed – cleanup. */
    public void shutdown() {
        expenseManager.close();
    }
}
