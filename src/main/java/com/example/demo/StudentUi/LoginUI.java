package com.example.demo.StudentUi;

import com.example.demo.StudentDao.StudentDAO;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginUI extends Application {

    @Override
    public void start(Stage stage) {
        // === HEADER ===
        Label emoji = new Label("🎓");
        emoji.setStyle("-fx-font-size: 48px;");

        Label titleLabel = new Label("Student Portal");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2d3748;");

        Label subtitleLabel = new Label("Sign in to continue ✨");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #718096;");

        VBox headerBox = new VBox(8, emoji, titleLabel, subtitleLabel);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(0, 0, 30, 0));

        // === FORM FIELDS ===
        Label userLabel = new Label("Username");
        userLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #4a5568; -fx-padding: 0 0 6 4;");

        TextField txtUser = new TextField();
        txtUser.setPromptText("👤  Enter your username");
        txtUser.setStyle(getInputStyle());
        txtUser.setPrefHeight(48);

        Label passLabel = new Label("Password");
        passLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #4a5568; -fx-padding: 0 0 6 4;");

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("🔒  Enter your password");
        txtPass.setStyle(getInputStyle());
        txtPass.setPrefHeight(48);

        VBox userField = new VBox(4, userLabel, txtUser);
        VBox passField = new VBox(4, passLabel, txtPass);

        // === LOGIN BUTTON ===
        Button btnLogin = new Button("🚀 Sign In");
        btnLogin.setPrefHeight(50);
        btnLogin.setPrefWidth(Double.MAX_VALUE);
        btnLogin.setStyle(getButtonStyle());

        // Button hover effects
        btnLogin.setOnMouseEntered(e -> {
            btnLogin.setStyle(getButtonHoverStyle());
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), btnLogin);
            scale.setToX(1.02);
            scale.setToY(1.02);
            scale.play();
        });
        btnLogin.setOnMouseExited(e -> {
            btnLogin.setStyle(getButtonStyle());
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), btnLogin);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        // === STATUS LABEL ===
        Label lblStatus = new Label();
        lblStatus.setStyle("-fx-text-fill: #e53e3e; -fx-font-size: 13px;");
        lblStatus.setAlignment(Pos.CENTER);
        lblStatus.setMaxWidth(Double.MAX_VALUE);

        // === LOGIN ACTION ===
        StudentDAO dao = new StudentDAO();

        btnLogin.setOnAction(e -> {
            String user = txtUser.getText().trim();
            String pass = txtPass.getText();

            if (user.isEmpty() || pass.isEmpty()) {
                lblStatus.setText("⚠️ Please fill in all fields");
                lblStatus.setStyle("-fx-text-fill: #ed8936; -fx-font-size: 13px;");
                shakeNode(btnLogin);
                return;
            }

            if (dao.login(user, pass)) {
                lblStatus.setText("✅ Login successful!");
                lblStatus.setStyle("-fx-text-fill: #38a169; -fx-font-size: 13px;");

                // Success animation
                FadeTransition fade = new FadeTransition(Duration.millis(300), stage.getScene().getRoot());
                fade.setToValue(0);
                fade.setOnFinished(ev -> {
                    new DashboardUI().show();
                    stage.close();
                });
                fade.play();
            } else {
                lblStatus.setText("❌ Invalid username or password");
                lblStatus.setStyle("-fx-text-fill: #e53e3e; -fx-font-size: 13px;");
                shakeNode(txtUser);
                shakeNode(txtPass);
            }
        });

        // Enter key support
        txtPass.setOnAction(e -> btnLogin.fire());
        txtUser.setOnAction(e -> txtPass.requestFocus());

        // === FOOTER ===
        Label footerLabel = new Label("Made with 💖 for students");
        footerLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #a0aec0;");
        footerLabel.setPadding(new Insets(20, 0, 0, 0));

        // === FORM CONTAINER ===
        VBox formBox = new VBox(18,
                userField,
                passField,
                new Region() {
                    {
                        setPrefHeight(5);
                    }
                },
                btnLogin,
                lblStatus);

        // === CARD CONTAINER ===
        VBox card = new VBox(0, headerBox, formBox, footerLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40, 45, 35, 45));
        card.setMaxWidth(380);
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.97);" +
                        "-fx-background-radius: 24;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.18), 25, 0, 0, 10);");

        // === ROOT CONTAINER ===
        StackPane root = new StackPane(card);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #667eea, #764ba2);");
        root.setPadding(new Insets(40));

        // === SCENE & STAGE ===
        Scene scene = new Scene(root, 500, 580);

        stage.setScene(scene);
        stage.setTitle("🎓 Student Portal - Login");
        stage.setResizable(false);

        // Fade-in animation
        root.setOpacity(0);
        stage.setOnShown(e -> {
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });

        stage.show();

        // Focus on username field
        txtUser.requestFocus();
    }

    private String getInputStyle() {
        return "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #e2e8f0;" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 2;" +
                "-fx-padding: 12 18;" +
                "-fx-font-size: 14px;" +
                "-fx-prompt-text-fill: #a0aec0;";
    }

    private String getButtonStyle() {
        return "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 12;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.4), 10, 0, 0, 4);";
    }

    private String getButtonHoverStyle() {
        return "-fx-background-color: linear-gradient(to right, #5a6fd6, #6a4190);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 15px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 12;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.6), 15, 0, 0, 6);";
    }

    private void shakeNode(javafx.scene.Node node) {
        javafx.animation.TranslateTransition shake = new javafx.animation.TranslateTransition(Duration.millis(50),
                node);
        shake.setFromX(0);
        shake.setByX(8);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }

    public static void main(String[] args) {
        launch();
    }
}
