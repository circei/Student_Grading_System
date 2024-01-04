
import java.util.*;

public class Main {
    private static Map<String, String> userCredentials = new HashMap<>();
    static DataInitializer dataInitializer = new DataInitializer();
    static List<Teacher> teachers = dataInitializer.initializeTeachers("Teachers.txt");
    static List<Student> students = dataInitializer.initializeStudents("Students.txt");
    public static List<Student> getAllStudents(){
        return students;
    }
    static {
        // Predefined user credentials
        userCredentials.put("Constantin George", "berbecul");
        userCredentials.put("Razvan Dragus", "regele");
        userCredentials.put("Costin Georgescu", "pisicuta");
        userCredentials.put("Matei Papusaru", "sportivul");
        userCredentials.put("Jugaru Cosmin","razaDeSoare");
        userCredentials.put("Camil Laurentiu", "ploiesti");
        userCredentials.put("Teodor Gandila","vin");


    }
    public static void main(String[] args) {



      /*  for (Teacher teacher : teachers) {
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
         */




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
                return authenticate(student, scanner);
            }
        }

        // Check if the provided username corresponds to an existing teacher
        for (Teacher teacher : teachers) {
            if (teacher.getUsername().equals(username)) {
                return authenticate(teacher, scanner);
            }
        }

        // Username not found
        System.out.println("User not in the system");
        return null;
    }
    private static User authenticate(User user, Scanner scanner) {
        System.out.println("Enter password:");
        String enteredPassword = scanner.nextLine();

        // Check if the entered username is in the userCredentials map
        if (userCredentials.containsKey(user.getUsername())) {
            // Retrieve the stored password based on the username
            String storedPassword = userCredentials.get(user.getUsername());

            // Check if the entered password matches the stored password
            if (storedPassword != null && storedPassword.equals(enteredPassword)) {
                System.out.println("Authentication successful.");
                return user;
            }
        }

        // If the username is not in the map or the password doesn't match

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
                    seeYourGrades(student,scanner);
                    break;
                case 2:
                    yourFinalGrades(student,scanner);
                    break;
                // Add more cases as needed
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    private static void yourFinalGrades(Student student, Scanner scanner) {
            Map<Subject, Double> finalGrades = student.calculateFinalGradeForEachSubject();
            System.out.println("Final Grades for " + student.getName() + ":");
            for (Map.Entry<Subject, Double> entry : finalGrades.entrySet()) {
                Subject subject = entry.getKey();
                double finalGrade = entry.getValue();

                System.out.println("Subject: " + subject.getName() + ", Final Grade: " + finalGrade);
            }

    }

    private static void seeYourGrades(Student student, Scanner scanner) {

        // Allow the teacher to choose a student
        // Retrieve the grades for the selected student
            Map<Subject, List<Grade>> gradesBySubject = student.getGradesBySubject();

            System.out.println("Grades for " + student.getName() + ":");
            for (Map.Entry<Subject, List<Grade>> entry : gradesBySubject.entrySet()) {
                Subject subject = entry.getKey();
                List<Grade> grades = entry.getValue();

                System.out.println("Subject: " + subject.getName());
                for (Grade grade : grades) {
                    System.out.println("   Grade: " + grade.getValue() + ", Date: " + grade.getInserationDate());
                }
            }

    }

    private static void teacherMenu(Teacher teacher, Scanner scanner) {
        // Implement the menu for teachers
        // You can provide options such as adding grades, managing subjects, etc.
        teacher.initializePermissions();
        boolean exit = false;
        while (!exit) {
            System.out.println("Welcome, " + teacher.getName() + " (Teacher)!");
            System.out.println("1. Add Grades");
            System.out.println("2. See Grades for a Student");
            System.out.println("3. Calculate Final Grades for a Student");
            System.out.println("4. Calculate Final Grade For One Subject");
            // Add more options as needed
            int choice = scanner.nextInt();
            // Implement logic based on the user's choice
            switch (choice) {
                case 1:
                    addGrades(teacher, scanner);
                    break;
                case 2:
                    seeGradesForStudent(teacher, scanner);
                    break;
                case 3:
                    calculateFinalGradesForStudent(teacher, scanner);
                    break;
                case 4:
                    calculateFinalGradeForOneSubject(teacher,scanner);
                    break;
                // Add more cases as needed
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    private static void calculateFinalGradeForOneSubject(Teacher teacher,Scanner scanner){
        System.out.println("Select a subject:");

        // Display a numbered list of subjects
        int subjectNumber = 1;
        for (Map.Entry<String, Subject> entry : teacher.getSubjects().entrySet()) {
            System.out.println(subjectNumber + ". " + entry.getKey());
            subjectNumber++;
        }

        int selectedNumber = scanner.nextInt();
        // Ensure the selected number is within the valid range
        if (selectedNumber >= 1 && selectedNumber <= teacher.getSubjects().size()) {
            // Get the selected subject by iterating through the map entries
            System.out.println(teacher.getSubjects().values().toArray(new Subject[0])[selectedNumber - 1].calculateFinalGradeForThisSubject());
        } else {
            System.out.println("Invalid selection.");

        }
    }
    private static void calculateFinalGradesForStudent(Teacher teacher, Scanner scanner) {
        System.out.println("Select a student:");

        // Display the list of available students
        List<Student> students = getAllStudents();  // Replace with your method to get all students
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i).getName());
        }

        // Allow the teacher to choose a student
        int selectedOption = scanner.nextInt();
        if (selectedOption >= 1 && selectedOption <= students.size()) {
            Student selectedStudent = students.get(selectedOption - 1);

            // Calculate final grades for the selected student
            Map<Subject, Double> finalGrades = selectedStudent.calculateFinalGradeForEachSubject();

            System.out.println("Final Grades for " + selectedStudent.getName() + ":");
            for (Map.Entry<Subject, Double> entry : finalGrades.entrySet()) {
                Subject subject = entry.getKey();
                double finalGrade = entry.getValue();

                System.out.println("Subject: " + subject.getName() + ", Final Grade: " + finalGrade);
            }
        } else {
            System.out.println("Invalid selection.");
        }
    }
    private static void seeGradesForStudent(Teacher teacher, Scanner scanner) {
        System.out.println("Select a student:");

        // Display the list of available students
        List<Student> students = getAllStudents();  // Replace with your method to get all students
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i).getName());
        }

        // Allow the teacher to choose a student
        int selectedOption = scanner.nextInt();
        if (selectedOption >= 1 && selectedOption <= students.size()) {
            Student selectedStudent = students.get(selectedOption - 1);

            // Retrieve the grades for the selected student
            Map<Subject, List<Grade>> gradesBySubject = selectedStudent.getGradesBySubject();

            System.out.println("Grades for " + selectedStudent.getName() + ":");
            for (Map.Entry<Subject, List<Grade>> entry : gradesBySubject.entrySet()) {
                Subject subject = entry.getKey();
                List<Grade> grades = entry.getValue();

                System.out.println("Subject: " + subject.getName());
                for (Grade grade : grades) {
                    System.out.println("   Grade: " + grade.getValue() + ", Date: " + grade.getInserationDate());
                }
            }
        } else {
            System.out.println("Invalid selection.");
        }
    }
    private static void addGrades(Teacher teacher, Scanner scanner) {
        // Implement logic to add grades here
        Subject subject = selectSubject(teacher, scanner);

        if (subject != null) {
            Student student = selectStudent(subject, scanner);

            if (student != null) {
                System.out.println("Enter grade value:");
                double gradeValue = scanner.nextDouble();

                // Add the grade to the subject for the student
                subject.addGrade(student, gradeValue, new Date(), teacher);

            }
        }
    }
    private static Subject selectSubject(Teacher teacher, Scanner scanner) {
        System.out.println("Select a subject:");

        // Display a numbered list of subjects
        int subjectNumber = 1;
        for (Subject subject : DataInitializer.getAllSubjects()) {
            System.out.println(subjectNumber + ". " + subject.getName());
            subjectNumber++;
        }

        int selectedNumber = scanner.nextInt();

        // Ensure the selected number is within the valid range
        if (selectedNumber >= 1 && selectedNumber <= DataInitializer.getAllSubjects().size()) {
            // Get the selected subject by iterating through the map entries
            return DataInitializer.getAllSubjects().toArray(new Subject[0])[selectedNumber - 1];
        } else {
            System.out.println("Invalid selection.");
            return null;
        }
    }
    private static Student selectStudent(Subject subject, Scanner scanner) {
        List<Student> studentsWithSubject = subject.getEnrolledStudents();
        System.out.println("Do you want to sort the student list alphabetically? (yes/no)");
        String sortOption = scanner.next().toLowerCase();

        if (sortOption.equals("yes")) {
            System.out.println("Select a student:");
            sortStudentListAlphabetically(studentsWithSubject);
            int selectedNumber = scanner.nextInt();
            // Ensure the selected number is within the valid range
            if (selectedNumber >= 1 && selectedNumber <= studentsWithSubject.size()) {
                return studentsWithSubject.get(selectedNumber - 1);
            } else {
                System.out.println("Invalid selection.");
                return null;
            }
        }
        else{
            System.out.println("Select a student:");
            int studentNumber = 1;
            for (Student student : studentsWithSubject) {
                System.out.println(studentNumber + ". " + student.getName());
                studentNumber++;
            }

            int selectedNumber = scanner.nextInt();
            // Ensure the selected number is within the valid range
            if (selectedNumber >= 1 && selectedNumber <= studentsWithSubject.size()) {
                return studentsWithSubject.get(selectedNumber - 1);
            } else {
                System.out.println("Invalid selection.");
                return null;
            }
        }



        // Display a numbered list of students who have the selected subject


    }

    private static void sortStudentListAlphabetically(List<Student> studentsWithSubject) {
        // Implement logic to sort the student list alphabetically
        Collections.sort(studentsWithSubject, Comparator.comparing(Student::getName));

        // Display the sorted list
        System.out.println("Sorted Student List Alphabetically:");
        for (int i = 0; i < studentsWithSubject.size(); i++) {
            System.out.println((i + 1) + ". " + studentsWithSubject.get(i).getName());
        }
    }



    private static void manageSubjects(Teacher teacher, Scanner scanner) {
        // Implement logic to manage subjects here
        // For example, adding or removing subjects
        // You can use the methods available in the Teacher class
    }
}