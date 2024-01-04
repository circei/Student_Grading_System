import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class Student implements User {
    private String name;
    private String password = "student";
    private Map<Subject, List<Grade>> gradesBySubject;

    public Student(String name) {
        this.name = name;
        this.gradesBySubject = new HashMap<>();
        File file = new File("grades_" + getName() + ".txt");
        if (file.exists()) {
            // Read data from the file and initialize gradesBySubject
            initializeGradesFromFile(file);
        }

    }
    public boolean authenticate(String enteredPassword) {
        return this.password.equals(enteredPassword);
    }

    public String getName() {
        return name;
    }

    public Map<Subject, List<Grade>> getGradesBySubject() {
        return gradesBySubject;
    }

    public void addGrade(Subject subject, double value, Date insertionDate, Teacher teacher) {
        // Check if the subject is associated with a teacher
        if (subject.getTeacher() != null) {


            // Check if the teacher has the permission to add grades for the specified subject
            if (teacher.hasSubjectPermissions(subject.getName())) {
                Grade grade = new Grade(value, insertionDate);

                if (!gradesBySubject.containsKey(subject)) {
                    gradesBySubject.put(subject, new ArrayList<>());
                }

                gradesBySubject.get(subject).add(grade);
                saveGradesToFile(subject,grade);

                System.out.println("Grade added successfully.");
            } else {
                // Handle the case where the teacher does not have the necessary permission
                System.out.println("Teacher does not have permission to add grades for " + subject.getName());

            }
        } else {
            // Handle the case where the subject is not associated with a teacher
            System.out.println("Subject is not associated with a teacher");
        }
    }
    public void addSubject(Subject subject) {
        if (!gradesBySubject.containsKey(subject)) {
            gradesBySubject.put(subject, new ArrayList<>());
        }
    }

    @Override
    public String getUsername() {
        // Assuming the student's name can be used as a username
        return name;
    }
    @Override
    public String getRole() {
        return "STUDENT";
    }

    @Override
    public Set<String> getPermissions() {
        // Assuming that students only have READ permission
        return Collections.singleton("READ");
    }


    public Map<Subject, Double> calculateFinalGradeForEachSubject() {
        Map<Subject, Double> finalGrades = new HashMap<>();

        for (Map.Entry<Subject, List<Grade>> entry : gradesBySubject.entrySet()) {
            Subject subject = entry.getKey();
            List<Grade> grades = entry.getValue();

            // Calculate the average final grade for the subject
            double averageGrade = grades.stream().mapToDouble(Grade::getValue).average().orElse(0.0);

            // Store the final grade in the result map
            finalGrades.put(subject, averageGrade);
        }

        return finalGrades;
    }


    public void saveGradesToFile(Subject subject, Grade grade) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("grades_" + getName() + ".txt", true))) {
            // Append the specific grade for the given subject
            writer.println("Subject: " + subject.getName());
            writer.println("Grade: " + grade.getValue());
            writer.println("Date: " + grade.getInserationDate());
            writer.println("-----");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeGradesFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Subject currentSubject = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Subject: ")) {
                    // Extract subject name and create a new Subject object
                    String subjectName = line.substring("Subject: ".length());
                    currentSubject = new Subject(subjectName, null); // You may need to provide a Teacher here
                    gradesBySubject.put(currentSubject, new ArrayList<>());
                } else if (line.startsWith("Grade: ")) {
                    // Extract grade value
                    double gradeValue = Double.parseDouble(line.substring("Grade: ".length()));

                    // Read next line to get the date (assuming it is formatted properly)
                    String dateString = reader.readLine();
                    Date gradeDate = parseDate(dateString);

                    Grade grade = new Grade(gradeValue, gradeDate);
                    // Add the grade to the current subject
                    gradesBySubject.get(currentSubject).add(grade);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private Date parseDate(String dateLine) {
        Date gradeDate = null;
        try {
            // Adjust the date format pattern to match the actual format
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            gradeDate = dateFormat.parse(dateLine.substring("Date: ".length()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return gradeDate;
    }


}
