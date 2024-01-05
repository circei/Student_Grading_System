
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
        for(Student student : students){
            student.initializeGradesFromList();
        }
        while(true) {
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

    }
    private static User authenticateUser(Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine();

        for (Student student : students) {
            if (student.getUsername().equals(username)) {
                return authenticate(student, scanner);
            }
        }

        for (Teacher teacher : teachers) {
            if (teacher.getUsername().equals(username)) {
                return authenticate(teacher, scanner);
            }
        }

        System.out.println("User not in the system");
        return null;
    }
    private static User authenticate(User user, Scanner scanner) {
        System.out.println("Enter password:");
        String enteredPassword = scanner.nextLine();


        if (userCredentials.containsKey(user.getUsername())) {
            String storedPassword = userCredentials.get(user.getUsername());
            if (storedPassword != null && storedPassword.equals(enteredPassword)) {
                System.out.println("Authentication successful.");
                return user;
            }
        }
        return null;
    }

    private static void studentMenu(Student student, Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("Welcome, " + student.getName() + " (Student)!");
            System.out.println("1. View Grades");
            System.out.println("2. Calculate Final Grades");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    seeYourGrades(student,scanner);
                    break;
                case 2:
                    yourFinalGrades(student,scanner);
                    break;
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
        teacher.initializePermissions();
        boolean exit = false;
        while (!exit) {
            System.out.println("Welcome, " + teacher.getName() + " (Teacher)!");
            System.out.println("1. Add Grades");
            System.out.println("2. Delete A Grade");
            System.out.println("3. Update A Grade");
            System.out.println("4. See Grades for a Student");
            System.out.println("5. Calculate Final Grades for a Student");
            System.out.println("6. Calculate Final Grade For One Subject");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addGrades(teacher, scanner);
                    break;
                case 2:
                    deleteGrade(teacher,scanner);
                    break;
                case 3:
                    //updateGrade(teacher,scanner);
                    break;
                case 4:
                    seeGradesForStudent(teacher, scanner);
                    break;
                case 5:
                    calculateFinalGradesForStudent(teacher, scanner);
                    break;
                case 6:
                    calculateFinalGradeForOneSubject(teacher,scanner);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    private static void calculateFinalGradeForOneSubject(Teacher teacher,Scanner scanner){
        System.out.println("Select a subject:");

        int subjectNumber = 1;
        for (Map.Entry<String, Subject> entry : teacher.getSubjects().entrySet()) {
            System.out.println(subjectNumber + ". " + entry.getKey());
            subjectNumber++;
        }

        int selectedNumber = scanner.nextInt();

        if (selectedNumber >= 1 && selectedNumber <= teacher.getSubjects().size()) {
            System.out.println(teacher.getSubjects().values().toArray(new Subject[0])[selectedNumber - 1].calculateFinalGradeForThisSubject());
        } else {
            System.out.println("Invalid selection.");

        }
    }
    private static void calculateFinalGradesForStudent(Teacher teacher, Scanner scanner) {
        List<Student> students = getAllStudents();
        System.out.println("Select a student:");
        System.out.println("Do you want to sort the student list alphabetically? (yes/no)");
        String sortOption = scanner.next().toLowerCase();

        if (sortOption.equals("yes")) {
            System.out.println("Select a student:");
            sortStudentListAlphabetically(students);
        }
        else{
            System.out.println("Select a student:");
            for (int i = 0; i < students.size(); i++) {
                System.out.println((i + 1) + ". " + students.get(i).getName());
            }
        }

        int selectedOption = scanner.nextInt();
        if (selectedOption >= 1 && selectedOption <= students.size()) {
            Student selectedStudent = students.get(selectedOption - 1);

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
        List<Student> students = getAllStudents();
        System.out.println("Select a student:");
        System.out.println("Do you want to sort the student list alphabetically? (yes/no)");
        String sortOption = scanner.next().toLowerCase();

        if (sortOption.equals("yes")) {
            System.out.println("Select a student:");
            sortStudentListAlphabetically(students);
        }
        else{
            System.out.println("Select a student:");
            for (int i = 0; i < students.size(); i++) {
                System.out.println((i + 1) + ". " + students.get(i).getName());
            }
        }
        int selectedOption = scanner.nextInt();
        if (selectedOption >= 1 && selectedOption <= students.size()) {
            Student selectedStudent = students.get(selectedOption - 1);
            System.out.println("Do you want to sort each subject's grades by date? (yes/no)");
            String sortOption2 = scanner.next().toLowerCase();

            Map<Subject, List<Grade>> gradesBySubject = selectedStudent.getGradesBySubject();
            if (sortOption2.equals("yes")) {
                gradesBySubject.forEach((subject, grades) -> grades.sort(Comparator.comparing(Grade::getInserationDate)));
            }

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

        Subject subject = selectSubject(teacher, scanner);

        if (subject != null) {
            Student student = selectStudent(subject, scanner);

            if (student != null) {
                System.out.println("Enter grade value:");
                double gradeValue = scanner.nextDouble();

                subject.addGrade(student, gradeValue, new Date(), teacher);

            }
        }
    }
    private static void deleteGrade(Teacher teacher, Scanner scanner) {

        Subject subject = selectSubject(teacher, scanner);

        if (subject != null) {
            Student student = selectStudent(subject, scanner);
            List<Grade> gradesForSelectedSubject = student.getGradesBySubject().get(subject);
            if (student != null && gradesForSelectedSubject != null) {
                System.out.println("Do you want to sort each subject's grades by date? (yes/no)");
                String sortOption = scanner.next().toLowerCase();


                if (sortOption.equals("yes")) {
                    Collections.sort(gradesForSelectedSubject, Comparator.comparing(Grade::getInserationDate));
                }

                System.out.println("Grades for " + student.getName() + "at subject "+subject.getName()+" :");
                for (Grade grade : gradesForSelectedSubject) {
                        System.out.println("   Grade: " + grade.getValue() + ", Date: " + grade.getInserationDate());
                }
                System.out.println("Enter grade index you want to delete: ");
                int index = scanner.nextInt();

                student.deleteGrade(student, index, teacher, subject);

            }
        }
    }
    private static Subject selectSubject(Teacher teacher, Scanner scanner) {
        System.out.println("Select a subject:");

        int subjectNumber = 1;
        for (Subject subject : DataInitializer.getAllSubjects()) {
            System.out.println(subjectNumber + ". " + subject.getName());
            subjectNumber++;
        }

        int selectedNumber = scanner.nextInt();

        if (selectedNumber >= 1 && selectedNumber <= DataInitializer.getAllSubjects().size()) {
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
            if (selectedNumber >= 1 && selectedNumber <= studentsWithSubject.size()) {
                return studentsWithSubject.get(selectedNumber - 1);
            } else {
                System.out.println("Invalid selection.");
                return null;
            }
        }
    }

    private static void sortStudentListAlphabetically(List<Student> studentsWithSubject) {
        Collections.sort(studentsWithSubject, Comparator.comparing(Student::getName));
        System.out.println("Sorted Student List Alphabetically:");
        for (int i = 0; i < studentsWithSubject.size(); i++) {
            System.out.println((i + 1) + ". " + studentsWithSubject.get(i).getName());
        }
    }
}