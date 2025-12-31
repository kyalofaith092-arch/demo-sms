package com.example.demo;

import com.example.demo.StudentUi.LoginUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Show the cute login UI
        new LoginUI().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
