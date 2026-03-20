# 💰 Personnel Expenses Tracker

A desktop-based personal expense management application built with **Java 17** and **JavaFX 17**.

**Course:** Principles of Programming and Data Structures  
**Student:** Saimon Shrestha | **ID:** 2531303 | **Roll:** 2510412281  
**Section:** L4 (A) | **College:** ATAN College for Professional Studies  
**Assignment:** Week 9 – OOP Architecture and Implementation

---

## 📋 Features

- ✅ **Add** new expense entries (amount, category, date, description)
- ✅ **Edit** existing expenses by clicking a row in the table
- ✅ **Delete** expenses with a confirmation dialog
- ✅ **View All** expenses in a sortable table
- ✅ **Filter** by category, month, and year
- ✅ **Generate Reports** with category-wise summaries and grand totals
- ✅ **Persistent Storage** using SQLite (data is saved between sessions)
- ✅ **Input Validation** with user-friendly error alerts

---

## 🏗️ OOP Principles Applied

| Principle | How Applied |
|-----------|-------------|
| **Encapsulation** | All fields in `Expense`, `Category`, `BaseEntity` are `private` with getters/setters |
| **Inheritance** | `Expense` and `Category` both extend `BaseEntity` |
| **Polymorphism** | `toString()` overridden differently in `Expense` vs `Category` |
| **Abstraction** | `ReportGenerator` and `ExpenseManager` hide complex logic behind simple public methods |

---

## 🗂️ Project Structure

```
PersonnelExpensesTracker/
├── pom.xml                          ← Maven build file
├── README.md
└── src/main/
    ├── java/com/expensetracker/
    │   ├── MainApp.java             ← JavaFX entry point
    │   ├── MainController.java      ← UI Controller (MVC)
    │   ├── BaseEntity.java          ← Abstract base class
    │   ├── Expense.java             ← Expense model
    │   ├── Category.java            ← Category model
    │   ├── ExpenseManager.java      ← Business logic
    │   ├── ReportGenerator.java     ← Report generation
    │   └── DatabaseHelper.java      ← SQLite database layer
    └── resources/
        ├── com/expensetracker/
        │   └── main.fxml            ← JavaFX UI layout
        └── styles/
            └── styles.css           ← Application stylesheet
```

---

## 🚀 How to Run in IntelliJ IDEA

### Prerequisites
- **Java JDK 17** (or higher) installed
- **IntelliJ IDEA** (Community or Ultimate)
- **Maven** (bundled with IntelliJ)
- Internet connection for first run (to download dependencies)

### Steps

1. **Clone or download** this repository
   ```bash
   git clone https://github.com/YOUR_USERNAME/PersonnelExpensesTracker.git
   ```

2. **Open in IntelliJ IDEA**
   - Open IntelliJ → `File` → `Open` → Select the `PersonnelExpensesTracker` folder
   - IntelliJ will detect the `pom.xml` automatically and import as a Maven project

3. **Wait for Maven to download dependencies**
   - IntelliJ will automatically download JavaFX and SQLite JDBC from Maven Central
   - Watch the bottom progress bar — wait until it says "Sync Finished"

4. **Add JavaFX VM options** *(required for Java 17+)*
   - Go to `Run` → `Edit Configurations`
   - Click `+` → `Application`
   - Set **Main class** to `com.expensetracker.MainApp`
   - In **VM options**, add:
     ```
     --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
     ```
   > 💡 **Easier alternative:** Just use `mvn javafx:run` (see below) — no VM options needed

5. **Run with Maven** *(recommended – no VM options needed)*
   - Open the **Maven panel** (right side of IntelliJ)
   - Expand: `Plugins` → `javafx` → double-click `javafx:run`
   - OR open the Terminal inside IntelliJ and type:
     ```bash
     mvn javafx:run
     ```

6. The application window will open. The SQLite database file (`expenses.db`) is created automatically in the project root.

---

## 🗄️ Database

The application uses **SQLite** and automatically creates an `expenses.db` file in the project root directory on first run. No setup is required. Data persists between sessions.

**Table: `expenses`**

| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER (PK) | Auto-increment unique ID |
| amount | REAL | Expense amount |
| category | TEXT | Category name |
| date | TEXT | Date (YYYY-MM-DD) |
| description | TEXT | Short description |
| created_date | TEXT | Record creation date |

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | JDK 17 | Core programming language |
| JavaFX | 17.0.2 | Desktop GUI framework |
| SQLite | 3.43.0 | Local database storage |
| JDBC | Standard | Database connectivity |
| Maven | 3.x | Build and dependency management |
| IntelliJ IDEA | 2023+ | IDE |

---

## 📸 Application Layout

```
┌─────────────────────────────────────────────────────────────┐
│  💰 Personnel Expenses Tracker       Header Bar              │
├──────────────┬──────────────────────────────────────────────┤
│  ADD/EDIT    │  EXPENSE RECORDS TABLE                       │
│  FORM        │  ┌────┬──────────┬──────────┬────────┬──────┐│
│              │  │ ID │  Date    │ Category │ Amount │ Desc ││
│  [Amount]    │  ├────┼──────────┼──────────┼────────┼──────┤│
│  [Category]  │  │ 1  │ 2025-03  │ Food     │ 250.00 │ ...  ││
│  [Date]      │  │ 2  │ 2025-03  │ Transport│ 80.00  │ ...  ││
│  [Desc]      │  └────┴──────────┴──────────┴────────┴──────┘│
│              │  Total Shown: Rs. 330.00                      │
│  [Add]       ├──────────────────────────────────────────────┤
│  [Clear]     │  EXPENSE SUMMARY REPORT (TextArea)           │
│  [Delete]    │  Food:       Rs. 250.00                      │
│              │  Transport:  Rs.  80.00                      │
│  FILTERS     │  TOTAL:      Rs. 330.00                      │
│  [Category]  │                                              │
│  [Month]     │                                              │
│  [Year]      │                                              │
│  [Filter]    │                                              │
│  [Report]    │                                              │
├──────────────┴──────────────────────────────────────────────┤
│  Status Bar: Ready.                                          │
└─────────────────────────────────────────────────────────────┘
```

---

## 👨‍💻 Author

**Saimon Shrestha**  
Student ID: 2531303 | Roll: 2510412281  
ATAN College for Professional Studies  
BSc Software Engineering – Year 1, Semester 2
