
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataInitializer dataInitializer = new DataInitializer();
        List<Teacher> teachers = dataInitializer.initializeTeachers("Teachers.txt");

        for (Teacher teacher : teachers) {
            System.out.println("Teacher: " + teacher.getName());
            System.out.println("Subjects: " + teacher.getSubjects().keySet());
        }
        List<Subject> allSubjects = DataInitializer.getAllSubjects();
        System.out.println("\nAll Subjects:");
        for (Subject subject : allSubjects) {
            System.out.println(subject.getName());
        }
    }
}