package com.example.university_management_app;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
public class LoginController {
    @FXML
    private  TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        authenticateUser(username, password);
    }
    public  void authenticateUser(String username, String password) {
        // Retrieve the password and role from the database
        String[] userDetails = getUserDetailsFromDatabase(username);

        if (userDetails != null && userDetails[0].equals(password)) {
            // Authentication successful
            System.out.println("Authentication successful for user: " + username);

            // Check the role
            String role = userDetails[1];
            if ("student".equals(role)) {
                // Load and display the student dashboard
                loadStudentDashboard();
            } else {
                // Handle other roles or scenarios
                System.out.println("User is not a student. Handle other roles here.");
            }
        } else {
            // Authentication failed
            System.out.println("Authentication failed for user: " + username);
            // Display an alert or error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authentication Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password!");
            alert.showAndWait();
        }
    }

    private static String[] getUserDetailsFromDatabase(String username) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1q2w3e");
            PreparedStatement statement = connection.prepareStatement("SELECT password, role FROM students WHERE username = ? UNION SELECT password, role FROM teachers WHERE username = ?");
            statement.setString(1, username);
            statement.setString(2, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the password and role from the result set
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                return new String[]{password, role};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private  void loadStudentDashboard() {
        try {
            // Load the student dashboard FXML
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("student_dashboard.fxml"));
            Parent root = loader.load();

            // Get the reference to the current scene
            Scene currentScene = usernameField.getScene();

            // Replace the content of the current scene with the student dashboard content
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
