import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
                allSubjects.add(newSubject);
            } else {
                teacher.addSubject(subjectName, existingSubject);
            }
        }
        return teacher;
    }

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
