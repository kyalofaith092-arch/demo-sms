# 🎓 Student Management System

A modern JavaFX desktop application for managing student records with a beautiful, animated UI and MariaDB backend.

![Java](https://img.shields.io/badge/Java-21-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-21.0.6-blue)
![MariaDB](https://img.shields.io/badge/MariaDB-3.3.3-brown)
![Gradle](https://img.shields.io/badge/Build-Gradle-green)

---

## ✨ Features

- **🔐 Secure Login** - Authentication with animated transitions
- **📊 Dashboard** - Beautiful glassmorphism-styled student management interface
- **➕ Add Students** - Create new student records
- **✏️ Update Students** - Click table row to auto-fill form, then edit
- **🗑️ Delete Students** - Remove student records with one click
- **🔍 Live Search** - Filter students by name in real-time
- **📈 Stats Display** - Shows total student count
- **🎨 Modern UI** - Smooth animations, hover effects, and premium styling

---

## 📁 Project Structure

```
demo/
├── build.gradle.kts              # Gradle build configuration
├── settings.gradle.kts           # Gradle settings
├── .gitignore                    # Git ignore rules
│
└── src/
    └── main/
        ├── java/
        │   ├── module-info.java              # Java module descriptor
        │   │
        │   └── com/example/demo/
        │       ├── Launcher.java             # Application entry point
        │       ├── MainApp.java              # Legacy/simple UI (unused)
        │       ├── DBconnection.java         # Database connection manager
        │       │
        │       ├── Student/
        │       │   └── Student.java          # Student model/entity class
        │       │
        │       ├── StudentDao/
        │       │   └── StudentDAO.java       # Data Access Object (CRUD operations)
        │       │
        │       └── StudentUi/
        │           ├── LoginUI.java          # Login screen with animations
        │           └── DashboardUI.java      # Main dashboard with table & form
        │
        └── resources/
            ├── db.properties                 # Database credentials (gitignored)
            │
            └── styles/
                └── dashboard.css             # Dashboard styling
```

---

## 🏗️ Architecture

| Layer | Component | Description |
|-------|-----------|-------------|
| **UI** | `LoginUI.java` | Login screen with gradient background and animations |
| **UI** | `DashboardUI.java` | Main dashboard with TableView, form, and CRUD buttons |
| **Model** | `Student.java` | POJO representing a student (id, name, course, age) |
| **DAO** | `StudentDAO.java` | Database operations: insert, update, delete, search, login |
| **Config** | `DBconnection.java` | Manages database connections from properties file |

---

## 🚀 Getting Started

### Prerequisites

- **Java 21** or higher
- **MariaDB** server running locally
- **Gradle** (or use the included wrapper)

### Database Setup

1. Create the database and tables:

```sql
CREATE DATABASE student_db;
USE student_db;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    course VARCHAR(100) NOT NULL,
    age INT NOT NULL
);

-- Insert a test user for login
INSERT INTO users (username, password) VALUES ('admin', 'admin123');
```

### Configuration

2. Create `src/main/resources/db.properties`:

```properties
db.url=jdbc:mariadb://localhost:3306/student_db
db.user=YOUR_USERNAME
db.password=YOUR_PASSWORD
```

> ⚠️ **Note:** This file is gitignored to protect your credentials.

### Running the Application

```bash
# Using Gradle wrapper
./gradlew run

# Or build a distribution
./gradlew jlink
```

---

## 🎨 UI Screenshots

### Login Screen
- Gradient purple background
- Glassmorphism card design
- Shake animation on invalid login
- Fade transition to dashboard

### Dashboard
- Header with emoji title and stats card
- Left panel: Form fields with field labels
- Right panel: Searchable TableView
- Animated button hover effects
- Tooltip notifications for actions

---

## 🔧 Key Technologies

| Technology | Purpose |
|------------|---------|
| **JavaFX 21** | Desktop UI framework |
| **MariaDB** | Relational database |
| **JDBC** | Database connectivity |
| **Gradle** | Build automation |
| **CSS** | Custom styling |

---

## 🧑‍💻 Development Notes

### Adding a New Feature
1. Add UI components in `DashboardUI.java`
2. Add database operations in `StudentDAO.java`
3. Update `Student.java` model if needed
4. Style with `dashboard.css`

### Table Selection → Form Binding
When a user clicks a row in the TableView, the selection listener automatically populates the form fields, making updates and deletes safer (no manual ID typing).

### Refresh Pattern
After every CRUD operation, call `refreshTable(table)` to reload data from the database.

---

## 📜 License

This project is for educational purposes.

---

## 👨‍💻 Author

Built with 💖 for students learning JavaFX and database integration.
