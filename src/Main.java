
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Date;
public class Main {
    private static Map<String, String> userCredentials = new HashMap<>();
    static DataInitializer dataInitializer = new DataInitializer();
    static List<Teacher> teachers = dataInitializer.initializeTeachers("Teachers.txt");
    static List<Student> students = dataInitializer.initializeStudents("Students.txt");
    static {
        // Predefined user credentials
        userCredentials.put("Constantin George", "berbecul");
        userCredentials.put("Razvan Dragus", "regele");
    }
    public static void main(String[] args) {



        for (Teacher teacher : teachers) {
            System.out.println("Teacher: " + teacher.getName());
            System.out.println("Subjects: " + teacher.getSubjects().keySet());
        }
        List<Subject> allSubjects = DataInitializer.getAllSubjects();
        System.out.println("\nAll Subjects:");
        for (Subject subject : allSubjects) {
            System.out.println(subject.getName());
            System.out.println(subject.getTeacher().getName());
        }


        // Print student information
        for (Student student : students) {
            System.out.println("\nStudent: " + student.getName());
            System.out.println("Subjects and Grades:");
            Map<Subject, List<Grade>> gradesBySubject = student.getGradesBySubject();

            for (Map.Entry<Subject, List<Grade>> entry : gradesBySubject.entrySet()) {
                Subject subject = entry.getKey();
                List<Grade> grades = entry.getValue();

                System.out.println("Subject: " + subject.getName());
                System.out.print("Grades: ");
                grades.stream().forEach(grade -> System.out.print(grade.getValue() + " "));
                System.out.println(); // Move to the next line for the next subject
            }
        }



        Scanner scanner = new Scanner(System.in);
        User currentUser = authenticateUser(scanner);
        if (currentUser != null) {
            if ("STUDENT".equals(currentUser.getRole())) {
                studentMenu((Student) currentUser, scanner);
            } else if ("TEACHER".equals(currentUser.getRole())) {
                teacherMenu((Teacher) currentUser, scanner);
            } else {
                System.out.println("Unknown user role.");
            }
        } else {
            System.out.println("Authentication failed.");
        }

    }
    private static User authenticateUser(Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine();



        // Check if the provided username corresponds to an existing student
        for (Student student : students) {
            if (student.getUsername().equals(username)) {
                System.out.println("Enter password:");
                String password = scanner.nextLine();

                // Check if the entered password is correct for the student
                if (student.authenticate(password)) {
                    return student;
                } else {
                    System.out.println("Authentication failed.");
                    return null;
                }
            }
        }

        // Check if the provided username corresponds to an existing teacher
        for (Teacher teacher : teachers) {
            if (teacher.getUsername().equals(username)) {
                System.out.println("Enter password:");
                String password = scanner.nextLine();

                // Check if the entered password is correct for the teacher
                if (teacher.authenticate(password)) {
                    return teacher;
                } else {
                    System.out.println("Authentication failed.");
                    return null;
                }
            }
        }

        // Username not found
        System.out.println("User not in the system");
        return null;
    }
    public static Student findStudentByName(String studentName) {
        for (Student student : students) {
            if (student.getName().equalsIgnoreCase(studentName)) {
                return student;
            }
        }
        return null; // Student not found
    }
    private static void studentMenu(Student student, Scanner scanner) {
        // Implement the menu for students
        // You can provide options such as viewing grades, calculating final grades, etc.
        boolean exit = false;
        while (!exit) {
            System.out.println("Welcome, " + student.getName() + " (Student)!");
            System.out.println("1. View Grades");
            System.out.println("2. Calculate Final Grades");
            // Add more options as needed
            int choice = scanner.nextInt();
            // Implement logic based on the user's choice
            switch (choice) {
                case 1:
                    // Logic to view grades
                    break;
                case 2:
                    // Logic to calculate final grades
                    break;
                // Add more cases as needed
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void teacherMenu(Teacher teacher, Scanner scanner) {
        // Implement the menu for teachers
        // You can provide options such as adding grades, managing subjects, etc.
        boolean exit = false;
        while (!exit) {
            System.out.println("Welcome, " + teacher.getName() + " (Teacher)!");
            System.out.println("1. Add Grades");
            System.out.println("2. Manage Subjects");
            // Add more options as needed
            int choice = scanner.nextInt();
            // Implement logic based on the user's choice
            switch (choice) {
                case 1:
                    addGrades(teacher, scanner);
                    break;
                case 2:
                    manageSubjects(teacher, scanner);
                    break;
                // Add more cases as needed
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    private static void addGrades(Teacher teacher, Scanner scanner) {
        // Implement logic to add grades here
        System.out.println("Enter student name:");
        scanner.nextLine();
        String studentName = scanner.nextLine();

        // Retrieve the student object based on the name
        Student student = findStudentByName(studentName);

        if (student != null) {
            System.out.println("Enter subject name:");
            String subjectName = scanner.next();

            // Retrieve the subject object based on the name
            Subject subject = teacher.getSubjects().get(subjectName);

            if (subject != null) {
                System.out.println("Enter grade value:");
                double gradeValue = scanner.nextDouble();

                // Add the grade to the subject for the student
                subject.addGrade(student, gradeValue, new Date());
                System.out.println("Grade added successfully.");
            } else {
                System.out.println("Subject not found.");
            }
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void manageSubjects(Teacher teacher, Scanner scanner) {
        // Implement logic to manage subjects here
        // For example, adding or removing subjects
        // You can use the methods available in the Teacher class
    }
}