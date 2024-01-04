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
    }
    public void initializeGradesFromList(){
        File file = new File("grades_" + getName() + ".txt");
        if (file.exists()) {
            initializeGradesFromFile(file);
        }
    }
    public String getName() {
        return name;
    }

    public Map<Subject, List<Grade>> getGradesBySubject() {
        return gradesBySubject;
    }

    public void addGrade(Subject subject, double value, Date insertionDate, Teacher teacher) {
        if (subject.getTeacher() != null) {
            if (teacher.hasSubjectPermissions(subject.getName())) {
                Grade grade = new Grade(value, insertionDate);

                if (!gradesBySubject.containsKey(subject)) {
                    gradesBySubject.put(subject, new ArrayList<>());
                }

                gradesBySubject.get(subject).add(grade);
                saveGradesToFile(subject,grade);

                System.out.println("Grade added successfully.");
            } else {
                System.out.println("Teacher does not have permission to add grades for " + subject.getName());

            }
        } else {
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
        return name;
    }
    @Override
    public String getRole() {
        return "STUDENT";
    }

    @Override
    public Set<String> getPermissions() {
        return Collections.singleton("READ");
    }

    public Map<Subject, Double> calculateFinalGradeForEachSubject() {
        Map<Subject, Double> finalGrades = new HashMap<>();

        for (Map.Entry<Subject, List<Grade>> entry : gradesBySubject.entrySet()) {
            Subject subject = entry.getKey();
            List<Grade> grades = entry.getValue();
            double averageGrade = grades.stream().mapToDouble(Grade::getValue).average().orElse(0.0);
            finalGrades.put(subject, averageGrade);
        }
        return finalGrades;
    }


    public void saveGradesToFile(Subject subject, Grade grade) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("grades_" + getName() + ".txt", true))) {
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
                    String subjectName = line.substring("Subject: ".length());
                    for (Map.Entry<Subject, List<Grade>> entry : gradesBySubject.entrySet()) {
                        Subject subject = entry.getKey();
                        if (subject.getName().equals(subjectName)) {
                            currentSubject = subject;
                            break;
                        }
                    }
                    gradesBySubject.put(currentSubject, new ArrayList<>());
                } else if (line.startsWith("Grade: ")) {
                    double gradeValue = Double.parseDouble(line.substring("Grade: ".length()));
                    String dateString = reader.readLine();
                    Date gradeDate = parseDate(dateString);
                    Grade grade = new Grade(gradeValue, gradeDate);
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
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            gradeDate = dateFormat.parse(dateLine.substring("Date: ".length()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gradeDate;
    }
}
