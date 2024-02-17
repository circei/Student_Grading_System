package com.example.university_management_app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class TeacherDashboard {
    private String username;
    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Subject> subjectsTable;

    @FXML
    private TableColumn<Subject, String> subjectNameColumn;

    @FXML
    private TableColumn<Subject, String> teacherColumn;


    @FXML
    private TableColumn<Subject, Integer> creditsColumn;

    public void setUsername(String username) {
        this.username=username;
    }

    private Connection conn;
    @FXML
    private Label facultyLabel;

    @FXML
    private Label specializationLabel;
    @FXML
    private void handleProfileButtonAction() {
        studentTable.setVisible(false);
        subjectsTable.setVisible(false);
        facultyLabel.setVisible(true);
        specializationLabel.setVisible(true);
        fetchAndDisplayProfileInfo(username);
    }
    private void fetchAndDisplayProfileInfo(String username) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT faculty, specialization FROM teachers WHERE username = ?");
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String faculty = rs.getString("faculty");
                String specialization = rs.getString("specialization");


                // Display fetched profile information
                facultyLabel.setText("Faculty: " + faculty);
                specializationLabel.setText("Specialization: " + specialization);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleSubjectsButtonAction(ActionEvent actionEvent) {
        subjectsTable.setVisible(true);
        studentTable.setVisible(false);
        facultyLabel.setVisible(false);
        specializationLabel.setVisible(false);
        subjectsTable.toFront();
        fetchAndDisplaySubjects();
    }

    public void handleStudentGradesButtonAction(ActionEvent actionEvent) {
    }
    @FXML
    private void initialize() {
        // Initialize database connection
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1q2w3e");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        subjectNameColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));

        subjectsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Check if single click
                // Get the selected subject
                Subject selectedSubject = subjectsTable.getSelectionModel().getSelectedItem();

                // Fetch and display students associated with the selected subject
                fetchAndDisplayStudents(selectedSubject);
            }
        });
        studentTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
                if (selectedStudent != null) {
                    openGradeInputDialog(selectedStudent);
                }
            }
        });



    }
    private void openGradeInputDialog(Student student) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Grade");
        alert.setHeaderText("Add grade for student: " + student.getUsername());
        alert.setContentText("Enter grade:");

        // Create a text input field for the grade
        TextField gradeField = new TextField();
        alert.getDialogPane().setContent(gradeField);

        // Add buttons for OK and Cancel
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, cancelButton);

        // Handle OK button click event
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == okButton) {
            String grade = gradeField.getText();
            // Now you can handle adding the grade to the database
            addGradeToDatabase(student, grade);
        }
    }
    private void addGradeToDatabase(Student student, String grade) {
        try {
            Subject selectedSubject = subjectsTable.getSelectionModel().getSelectedItem();
            // Prepare the SQL statement to insert the grade into the database
            String sql = "INSERT INTO student_grades (date, subject, grade, passed, username) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            // Set the parameters for the SQL statement
            statement.setDate(1, new java.sql.Date(System.currentTimeMillis())); // Set the current date
            statement.setString(2, selectedSubject.getSubjectName()); // Set the subject name
            statement.setInt(3, Integer.parseInt(grade)); // Set the grade
            statement.setBoolean(4, determinePassed(grade)); // Set whether the student passed based on the grade
            statement.setString(5, student.getUsername()); // Set the student's username

            // Execute the SQL statement
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new grade was added successfully!");
            }

            // Close the connection
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean determinePassed(String grade) {
        // Implement your logic to determine if the student passed based on the grade
        // For example, you might consider any grade above a certain threshold to be a pass
        double numericGrade = Double.parseDouble(grade);
        return numericGrade >= 5.0; // Assuming a passing grade is 5 or higher
    }

    private void fetchAndDisplayStudents(Subject selectedSubject) {
        try {
            // Prepare the SQL statement to select students based on the subject name
            PreparedStatement statement = conn.prepareStatement("SELECT username FROM subjects WHERE \"subjectName\" = ?");
            statement.setString(1, selectedSubject.getSubjectName());

            // Execute the query
            ResultSet rs = statement.executeQuery();

            // Populate a list of students associated with the selected subject
            ObservableList<Student> studentsList = FXCollections.observableArrayList();
            while (rs.next()) {
                // Assuming Student class has constructor with necessary parameters like username, subjectName, etc.
                Student student = new Student(rs.getString("username"));
                studentsList.add(student);
            }

            // Display the fetched students
            displayStudents(studentsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private TableView<Student> studentTable;
    private void displayStudents(ObservableList<Student> studentsList) {
        studentTable.setVisible(true);
        subjectsTable.setVisible(false);
        facultyLabel.setVisible(false);
        specializationLabel.setVisible(false);
        studentTable.setItems(studentsList);
        studentTable.toFront();

    }


    private void fetchAndDisplaySubjects() {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT subjectName FROM teacher_subjects WHERE username = ?");
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            ObservableList<Subject> subjectsList = FXCollections.observableArrayList();

            while (rs.next()) {
                String subjectName = rs.getString("subjectName");
                Subject subject = new Subject(subjectName);
                subjectsList.add(subject);
            }

            subjectsTable.setItems(subjectsList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
}
