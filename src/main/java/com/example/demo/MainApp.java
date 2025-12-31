package com.example.demo;

import com.example.demo.Student.Student;
import com.example.demo.StudentDao.StudentDAO;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class MainApp {

    StudentDAO dao = new StudentDAO();

    public void show() {

        TextField txtId = new TextField();
        txtId.setPromptText("ID");

        TextField txtName = new TextField();
        txtName.setPromptText("Name");

        TextField txtCourse = new TextField();
        txtCourse.setPromptText("Course");

        TextField txtAge = new TextField();
        txtAge.setPromptText("Age");

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Search by name");

        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update");
        Button btnDelete = new Button("Delete");

        TableView<Student> table = new TableView<>();

        TableColumn<Student, Integer> c1 = new TableColumn<>("ID");
        c1.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getId()).asObject());

        TableColumn<Student, String> c2 = new TableColumn<>("Name");
        c2.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getName()));

        TableColumn<Student, String> c3 = new TableColumn<>("Course");
        c3.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCourse()));

        TableColumn<Student, Integer> c4 = new TableColumn<>("Age");
        c4.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getAge()).asObject());

        table.getColumns().addAll(c1, c2, c3, c4);

        // Helper to refresh table
        Runnable refreshTable = () -> table.setItems(dao.search(""));

        // Listener to populate fields on row selection
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtId.setText(String.valueOf(newVal.getId()));
                txtName.setText(newVal.getName());
                txtCourse.setText(newVal.getCourse());
                txtAge.setText(String.valueOf(newVal.getAge()));
            }
        });

        btnAdd.setOnAction(e -> {
            dao.insert(new Student(txtName.getText(), txtCourse.getText(),
                    Integer.parseInt(txtAge.getText())));
            refreshTable.run();
        });

        btnUpdate.setOnAction(e -> {
            dao.update(new Student(Integer.parseInt(txtId.getText()),
                    txtName.getText(), txtCourse.getText(),
                    Integer.parseInt(txtAge.getText())));
            refreshTable.run();
        });

        btnDelete.setOnAction(e -> {
            dao.delete(Integer.parseInt(txtId.getText()));
            refreshTable.run();
        });

        txtSearch.textProperty().addListener((obs, old, val) -> table.setItems(dao.search(val)));

        // Initial data load
        refreshTable.run();

        VBox form = new VBox(10, txtId, txtName, txtCourse, txtAge,
                btnAdd, btnUpdate, btnDelete, txtSearch);

        BorderPane root = new BorderPane();
        root.setLeft(form);
        root.setCenter(table);
        root.setPadding(new Insets(15));

        Stage stage = new Stage();
        stage.setScene(new Scene(root, 800, 450));
        stage.setTitle("Student Management Dashboard");
        stage.show();
    }
}
