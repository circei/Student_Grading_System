import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DataInitializer {
    private Connection connection;
    public DataInitializer(){
        try{
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","1q2w3e");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean teacherHasSubject(String teacherName, String subjectName) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM teacher_subjects WHERE teacher_name = ? AND subject_name = ?");
            statement.setString(1, teacherName);
            statement.setString(2, subjectName);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Returns true if there is a row matching the query
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static List<Subject> allSubjects = new ArrayList<>();

    private static Subject findSubjectByName(String subjectName) {

        for (Subject existingSubject : allSubjects) {
            if (existingSubject.getName().equals(subjectName)) {
                return existingSubject;
            }
        }
        return null;
    }

    public static List<Subject> getAllSubjects() {
        return allSubjects;
    }

    public List<Student> initializeStudents(String fileName) {
        List<Student> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student student = extractStudentFromLine(line);
                students.add(student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return students;
    }

    private Student extractStudentFromLine(String line) {
        String[] parts = line.split(":");
        String studentName = parts[0].trim();

        Student student = new Student(studentName);


        if (parts.length > 1) {
            String[] subjectNames = parts[1].split(",");
            for (String subjectName : subjectNames) {
                subjectName = subjectName.trim();
                Subject existingSubject = findSubjectByName(subjectName);

                if (existingSubject != null) {

                    student.addSubject(existingSubject);
                    existingSubject.enrollStudent(student);
                }
            }
        }

        return student;
    }
}
