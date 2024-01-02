import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DataInitializer {
    private static List<Subject> allSubjects = new ArrayList<>();
    public  List<Teacher> initializeTeachers(String fileName){
        List<Teacher> teachers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while((line = reader.readLine()) != null){
                Teacher teacher = extractTeacherFromLine(line);
                teachers.add(teacher);
            }

        } catch(IOException e){
            e.printStackTrace();
        }
        return teachers;
    }
    private  Teacher extractTeacherFromLine(String line){
        String[] parts = line.split(":");
        String teacherName = parts[0].trim();
        String[] subjectNames = parts[1].split(",");

        Teacher teacher = new Teacher(teacherName, DataInitializer.this);
        for (String subjectName : subjectNames) {
            Subject existingSubject = findSubjectByName(subjectName.trim());

            if (existingSubject == null) {
                Subject newSubject = new Subject(subjectName.trim(), teacher);
                teacher.addSubject(subjectName, newSubject);

                // Add the new subject to the list of all subjects
                allSubjects.add(newSubject);
            } else {
                // If the subject already exists, associate it with the current teacher
                teacher.addSubject(subjectName, existingSubject);
            }
        }
        return teacher;
    }

    private static Subject findSubjectByName(String subjectName) {
        // Search for the subject in the list of all subjects
        for (Subject existingSubject : allSubjects) {
            if (existingSubject.getName().equals(subjectName)) {
                return existingSubject;
            }
        }
        return null; // Subject not found
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

        // Check if there are subjects specified in the line
        if (parts.length > 1) {
            String[] subjectNames = parts[1].split(",");
            for (String subjectName : subjectNames) {
                subjectName = subjectName.trim();
                Subject existingSubject = findSubjectByName(subjectName);

                if (existingSubject != null) {
                    // If the subject already exists, associate it with the student
                    student.addSubject(existingSubject);
                    existingSubject.enrollStudent(student);
                }
                // You may want to handle the case where the subject doesn't exist,
                // depending on your specific requirements.
            }
        }

        return student;
    }



}
