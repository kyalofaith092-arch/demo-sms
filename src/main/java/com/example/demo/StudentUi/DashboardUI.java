package com.example.demo.StudentUi;

import com.example.demo.Student.Student;
import com.example.demo.StudentDao.StudentDAO;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DashboardUI {

    private StudentDAO dao = new StudentDAO();
    private Label statsLabel;

    public void show() {
        // === HEADER SECTION ===
        Label titleLabel = new Label("🎓 Student Dashboard");
        titleLabel.getStyleClass().add("header-title");

        Label subtitleLabel = new Label("Manage your students with style ✨");
        subtitleLabel.getStyleClass().add("header-subtitle");

        VBox headerBox = new VBox(2, titleLabel, subtitleLabel);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // === STATS CARD ===
        statsLabel = new Label("0");
        statsLabel.getStyleClass().add("stats-number");
        Label statsText = new Label("Total Students");
        statsText.getStyleClass().add("stats-label");

        VBox statsCard = new VBox(4, statsLabel, statsText);
        statsCard.getStyleClass().add("stats-card");
        statsCard.setAlignment(Pos.CENTER);
        statsCard.setPrefWidth(140);

        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(headerBox, Priority.ALWAYS);
        headerRow.getChildren().addAll(headerBox, statsCard);

        // === FORM FIELDS ===
        Label formTitle = new Label("📝 Student Details");
        formTitle.getStyleClass().add("form-title");

        TextField txtId = createStyledTextField("🆔  Student ID (Auto-generated)", false);
        txtId.getStyleClass().addAll("cute-text-field", "id-field");
        txtId.setEditable(false);

        TextField txtName = createStyledTextField("👤  Full Name", true);
        TextField txtCourse = createStyledTextField("📚  Course / Program", true);
        TextField txtAge = createStyledTextField("🎂  Age", true);

        // Field labels
        VBox idField = createFieldGroup("ID", txtId);
        VBox nameField = createFieldGroup("Full Name", txtName);
        VBox courseField = createFieldGroup("Course", txtCourse);
        VBox ageField = createFieldGroup("Age", txtAge);

        // === BUTTONS ===
        Button btnAdd = createStyledButton("➕ Add Student", "add-button");
        Button btnUpdate = createStyledButton("✏️ Update", "update-button");
        Button btnDelete = createStyledButton("🗑️ Delete", "delete-button");
        Button btnClear = createStyledButton("✨ Clear", "clear-button");

        // Button animations
        addButtonAnimation(btnAdd);
        addButtonAnimation(btnUpdate);
        addButtonAnimation(btnDelete);
        addButtonAnimation(btnClear);

        HBox buttonRow1 = new HBox(12, btnAdd, btnUpdate);
        buttonRow1.setAlignment(Pos.CENTER);
        HBox buttonRow2 = new HBox(12, btnDelete, btnClear);
        buttonRow2.setAlignment(Pos.CENTER);

        VBox buttonsBox = new VBox(10, buttonRow1, buttonRow2);

        // === FORM PANEL ===
        VBox formPanel = new VBox(14,
                formTitle,
                idField, nameField, courseField, ageField,
                new Region() {
                    {
                        setPrefHeight(10);
                    }
                },
                buttonsBox);
        formPanel.getStyleClass().add("form-panel");
        formPanel.setPrefWidth(280);

        // === SEARCH BAR ===
        TextField txtSearch = new TextField();
        txtSearch.setPromptText("🔍  Search students by name...");
        txtSearch.getStyleClass().addAll("cute-text-field", "search-field");
        txtSearch.setMaxWidth(350);

        HBox searchBox = new HBox(txtSearch);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 0, 16, 0));

        // === TABLE VIEW ===
        TableView<Student> table = new TableView<>();
        table.setPlaceholder(new Label("No students found 🎓\nAdd your first student!"));
        table.getStyleClass().add("table-view");

        TableColumn<Student, Integer> c1 = new TableColumn<>("ID");
        c1.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getId()).asObject());
        c1.setPrefWidth(70);
        c1.setStyle("-fx-alignment: CENTER;");

        TableColumn<Student, String> c2 = new TableColumn<>("Name");
        c2.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getName()));
        c2.setPrefWidth(180);

        TableColumn<Student, String> c3 = new TableColumn<>("Course");
        c3.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCourse()));
        c3.setPrefWidth(200);

        TableColumn<Student, Integer> c4 = new TableColumn<>("Age");
        c4.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getAge()).asObject());
        c4.setPrefWidth(80);
        c4.setStyle("-fx-alignment: CENTER;");

        table.getColumns().addAll(c1, c2, c3, c4);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Table container with rounded corners
        VBox tableContainer = new VBox(table);
        tableContainer.getStyleClass().add("table-container");
        VBox.setVgrow(table, Priority.ALWAYS);

        // === LOAD DATA ===
        refreshTable(table);

        // === TABLE SELECTION LISTENER ===
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Animate the form fill
                FadeTransition fade = new FadeTransition(Duration.millis(150), formPanel);
                fade.setFromValue(0.7);
                fade.setToValue(1.0);
                fade.play();

                txtId.setText(String.valueOf(newSelection.getId()));
                txtName.setText(newSelection.getName());
                txtCourse.setText(newSelection.getCourse());
                txtAge.setText(String.valueOf(newSelection.getAge()));
            }
        });

        // === BUTTON ACTIONS ===
        btnAdd.setOnAction(e -> {
            if (!txtName.getText().isEmpty() && !txtCourse.getText().isEmpty() && !txtAge.getText().isEmpty()) {
                try {
                    dao.insert(new Student(txtName.getText(), txtCourse.getText(),
                            Integer.parseInt(txtAge.getText())));
                    clearFields(txtId, txtName, txtCourse, txtAge);
                    refreshTable(table);
                    showNotification(btnAdd, "✅ Student added successfully!");
                } catch (NumberFormatException ex) {
                    showNotification(btnAdd, "⚠️ Please enter a valid age!");
                }
            } else {
                showNotification(btnAdd, "⚠️ Please fill all fields!");
            }
        });

        btnUpdate.setOnAction(e -> {
            Student selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    selected.setName(txtName.getText());
                    selected.setCourse(txtCourse.getText());
                    selected.setAge(Integer.parseInt(txtAge.getText()));
                    dao.update(selected);
                    clearFields(txtId, txtName, txtCourse, txtAge);
                    refreshTable(table);
                    showNotification(btnUpdate, "✅ Updated successfully!");
                } catch (NumberFormatException ex) {
                    showNotification(btnUpdate, "⚠️ Please enter a valid age!");
                }
            } else {
                showNotification(btnUpdate, "⚠️ Select a student first!");
            }
        });

        btnDelete.setOnAction(e -> {
            Student selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                dao.delete(selected.getId());
                clearFields(txtId, txtName, txtCourse, txtAge);
                refreshTable(table);
                showNotification(btnDelete, "🗑️ Student deleted!");
            } else {
                showNotification(btnDelete, "⚠️ Select a student first!");
            }
        });

        btnClear.setOnAction(e -> {
            clearFields(txtId, txtName, txtCourse, txtAge);
            table.getSelectionModel().clearSelection();
        });

        // === SEARCH LISTENER ===
        txtSearch.textProperty().addListener((obs, old, val) -> {
            table.setItems(dao.search(val));
            updateStats(table);
        });

        // === RIGHT PANEL (Table Section) ===
        VBox tableSection = new VBox(12, searchBox, tableContainer);
        VBox.setVgrow(tableContainer, Priority.ALWAYS);
        HBox.setHgrow(tableSection, Priority.ALWAYS);

        // === MAIN CONTENT ===
        HBox contentRow = new HBox(24, formPanel, tableSection);
        HBox.setHgrow(tableSection, Priority.ALWAYS);
        VBox.setVgrow(contentRow, Priority.ALWAYS);

        // === GLASS CARD CONTAINER ===
        VBox glassCard = new VBox(20, headerRow, contentRow);
        glassCard.getStyleClass().add("glass-card");
        VBox.setVgrow(contentRow, Priority.ALWAYS);

        // === ROOT CONTAINER ===
        StackPane root = new StackPane(glassCard);
        root.getStyleClass().add("main-container");
        root.setPadding(new Insets(24));

        // === SCENE & STAGE ===
        Scene scene = new Scene(root, 1000, 620);

        // Load CSS
        String css = getClass().getResource("/styles/dashboard.css").toExternalForm();
        scene.getStylesheets().add(css);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("🎓 Student Management Dashboard");
        stage.setMinWidth(900);
        stage.setMinHeight(550);

        // Add fade-in animation on show
        root.setOpacity(0);
        stage.setOnShown(e -> {
            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });

        stage.show();
    }

    // === HELPER METHODS ===

    private TextField createStyledTextField(String placeholder, boolean addStyle) {
        TextField field = new TextField();
        field.setPromptText(placeholder);
        if (addStyle) {
            field.getStyleClass().add("cute-text-field");
        }
        return field;
    }

    private VBox createFieldGroup(String labelText, TextField field) {
        Label label = new Label(labelText);
        label.getStyleClass().add("field-label");
        VBox group = new VBox(4, label, field);
        return group;
    }

    private Button createStyledButton(String text, String styleClass) {
        Button btn = new Button(text);
        btn.getStyleClass().addAll("cute-button", styleClass);
        btn.setPrefWidth(120);
        btn.setPrefHeight(42);
        return btn;
    }

    private void addButtonAnimation(Button btn) {
        btn.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), btn);
            scale.setToX(1.03);
            scale.setToY(1.03);
            scale.play();
        });
        btn.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), btn);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
    }

    private void showNotification(Button btn, String message) {
        Tooltip tooltip = new Tooltip(message);
        tooltip.setStyle("-fx-background-color: #2d3748; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-padding: 10 14; -fx-font-size: 13px;");
        tooltip.setAutoHide(true);
        tooltip.show(btn,
                btn.localToScreen(btn.getBoundsInLocal()).getMinX(),
                btn.localToScreen(btn.getBoundsInLocal()).getMinY() - 45);

        // Auto-hide after 2 seconds
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> tooltip.hide());
        pause.play();
    }

    private void refreshTable(TableView<Student> table) {
        ObservableList<Student> list = dao.search("");
        table.setItems(list);
        updateStats(table);
    }

    private void updateStats(TableView<Student> table) {
        if (statsLabel != null) {
            statsLabel.setText(String.valueOf(table.getItems().size()));
        }
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }
}
