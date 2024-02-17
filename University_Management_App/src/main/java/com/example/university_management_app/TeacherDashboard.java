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
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class TeacherDashboard {
    @FXML
    private TableView<Grade> gradesTable;
    @FXML
    private TableColumn<Grade, String> dateColumn;
    @FXML
    private TableColumn<Grade, String> subjectColumn;

    @FXML
    private TableColumn<Grade, Integer> gradeColumn;

    @FXML
    private TableColumn<Grade, String> passedColumn;
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
        gradesTable.setVisible(false);
        specializationLabel.setVisible(false);
        subjectsTable.toFront();
        fetchAndDisplaySubjects();
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
    @FXML
    private void initialize() {

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1q2w3e");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        subjectNameColumn.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        passedColumn.setCellValueFactory(new PropertyValueFactory<>("passed"));
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
        // Create a ComboBox for options
        ComboBox<String> options = new ComboBox<>();
        options.getItems().addAll("Add Grade", "See Grades");
        options.setValue("Add Grade"); // Default value

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Select Option");
        alert.setHeaderText("Select an option for student: " + student.getUsername());
        alert.getDialogPane().setContent(options);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == okButton) {
            String selectedOption = options.getValue();
            if (selectedOption.equals("Add Grade")) {
                openAddGradeDialog(student);
            } else if (selectedOption.equals("See Grades")) {
                subjectsTable.setVisible(false);
                studentTable.setVisible(false);
                facultyLabel.setVisible(false);
                gradesTable.setVisible(true);
                specializationLabel.setVisible(false);
                subjectsTable.toFront();
                fetchAndDisplaySubjects();
                populateGradesTable(student.getUsername());
            }
        }
    }
    private void openAddGradeDialog(Student student) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Grade");
        alert.setHeaderText("Add grade for student: " + student.getUsername());
        alert.setContentText("Enter grade:");

        TextField gradeField = new TextField();
        alert.getDialogPane().setContent(gradeField);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, cancelButton);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == okButton) {
                String grade = gradeField.getText();
                addGradeToDatabase(student, grade);
            }
        });
    }

    private void addGradeToDatabase(Student student, String grade) {
        try {
            Subject selectedSubject = subjectsTable.getSelectionModel().getSelectedItem();
            String sql = "INSERT INTO student_grades (date, subject, grade, passed, username) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setDate(1, new java.sql.Date(System.currentTimeMillis())); // Set the current date
            statement.setString(2, selectedSubject.getSubjectName()); // Set the subject name
            statement.setInt(3, Integer.parseInt(grade)); // Set the grade
            statement.setBoolean(4, determinePassed(grade)); // Set whether the student passed based on the grade
            statement.setString(5, student.getUsername()); // Set the student's username

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new grade was added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean determinePassed(String grade) {
        double numericGrade = Double.parseDouble(grade);
        return numericGrade >= 5.0;
    }

    private void fetchAndDisplayStudents(Subject selectedSubject) {
        try {

            PreparedStatement statement = conn.prepareStatement("SELECT username FROM subjects WHERE \"subjectName\" = ?");
            statement.setString(1, selectedSubject.getSubjectName());

            ResultSet rs = statement.executeQuery();

            ObservableList<Student> studentsList = FXCollections.observableArrayList();
            while (rs.next()) {
                Student student = new Student(rs.getString("username"));
                studentsList.add(student);
            }

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
