module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mariadb.jdbc;
    requires org.controlsfx.controls;

    opens com.example.demo to javafx.fxml;
    opens com.example.demo.StudentUi to javafx.fxml, javafx.graphics;
    opens com.example.demo.Student to javafx.base;

    exports com.example.demo;
    exports com.example.demo.StudentUi;
    exports com.example.demo.Student;
    exports com.example.demo.StudentDao;
}