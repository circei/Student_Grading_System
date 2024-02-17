package com.example.university_management_app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentDashboard {
    private Connection conn;
    private String username;
    @FXML
    private TableView<Grade> gradesTable;
    @FXML
    private TableView<Subject> subjectsTable;

    @FXML
    private TableColumn<Grade, String> dateColumn;
    @FXML
    private TableColumn<Subject,String> subjectNameColumn;
    @FXML
    private TableColumn<Subject,String> teacherNameColumn;
    @FXML
    private TableColumn<Subject,Integer> creditsColumn;
    @FXML
    private TableColumn<Grade, String> subjectColumn;

    @FXML
    private TableColumn<Grade, Integer> gradeColumn;

    @FXML
    private TableColumn<Grade, String> passedColumn;

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label facultyLabel;

    @FXML
    private Label specializationLabel;

    @FXML
    private Label yearOfStudyLabel;
    public void setUsername(String username) {
        this.username=username;
    }
    @FXML
    private void initialize() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1q2w3e");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        passedColumn.setCellValueFactory(new PropertyValueFactory<>("passed"));
        subjectNameColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));
    }


    @FXML
    private void handleProfileButtonAction() {
        gradesTable.setVisible(false);
        subjectsTable.setVisible(false);
        facultyLabel.setVisible(true);
        specializationLabel.setVisible(true);
        yearOfStudyLabel.setVisible(true);
        fetchAndDisplayProfileInfo(username);
    }
    private void fetchAndDisplayProfileInfo(String username) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT faculty, specialization, year FROM students WHERE username = ?");
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String faculty = rs.getString("faculty");
                String specialization = rs.getString("specialization");
                int yearOfStudy = rs.getInt("year");

                // Display fetched profile information
                facultyLabel.setText("Faculty: " + faculty);
                specializationLabel.setText("Specialization: " + specialization);
                yearOfStudyLabel.setText("Year of Study: " + yearOfStudy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCoursesButtonAction() {
        facultyLabel.setVisible(false);
        specializationLabel.setVisible(false);
        yearOfStudyLabel.setVisible(false);
        gradesTable.setVisible(false);
        subjectsTable.setVisible(true);
        populateSubjectsTable(username);
    }
    private void populateSubjectsTable( String username){
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT \"subjectName\", teacher, credits FROM subjects WHERE username = ?");
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            ObservableList<Subject> subjectsList = subjectsTable.getItems();
            subjectsList.clear(); // Clear previous data

            while (rs.next()) {
                String teacher = rs.getString("teacher");
                String subject = rs.getString("subjectName");
                int credits = rs.getInt("credits");


                Subject subjectObj = new Subject(subject, teacher, credits);
                subjectsList.add(subjectObj);

            }
            subjectsTable.setItems(subjectsList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGradesButtonAction() {
        facultyLabel.setVisible(false);
        specializationLabel.setVisible(false);
        yearOfStudyLabel.setVisible(false);
        subjectsTable.setVisible(false);
        gradesTable.setVisible(true);
        populateGradesTable(username);
    }

    @FXML
    private void handleScheduleButtonAction() {
        // Handle the action when the Schedule button is clicked
        // For example, you can load the schedule view
        System.out.println("Schedule button clicked");
    }

    @FXML
    private void handleLogoutButtonAction() {
        Stage stage = (Stage) welcomeLabel.getScene().getWindow();

        // Load the login scene
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Set the scene in the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateGradesTable(String username){
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT date, subject, grade, passed FROM student_grades WHERE username = ?");
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            ObservableList<Grade> gradesList = gradesTable.getItems();
            gradesList.clear(); // Clear previous data

            while (rs.next()) {
                String date = rs.getString("date");
                String subject = rs.getString("subject");
                int grade = rs.getInt("grade");
                String passed = rs.getString("passed");

                Grade gradeObj = new Grade(date, subject, grade, passed);
                gradesList.add(gradeObj);
            }
            gradesTable.setItems(gradesList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
