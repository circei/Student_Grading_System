import java.util.*;
public class Student implements User {
    private String name;
    private String password = "student";
    private Map<Subject, List<Grade>> gradesBySubject;

    public Student(String name) {
        this.name = name;
        this.gradesBySubject = new HashMap<>();

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

    public void addGrade(Subject subject, double value, Date insertionDate) {
        // Check if the subject is associated with a teacher
        if (subject.getTeacher() != null) {
            Teacher teacher = subject.getTeacher();

            // Check if the teacher has the permission to add grades for the specified subject
            if (teacher.hasSubjectPermissions("CREATE_GRADE_" + subject.getName())) {
                Grade grade = new Grade(value, insertionDate);

                if (!gradesBySubject.containsKey(subject)) {
                    gradesBySubject.put(subject, new ArrayList<>());
                }

                gradesBySubject.get(subject).add(grade);
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
}
