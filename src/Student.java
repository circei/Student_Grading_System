import java.util.*;
public class Student {
    private String name;
    private Map<Subject, List<Grade>> gradesBySubject;

    public Student(String name) {
        this.name = name;
        this.gradesBySubject = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Map<Subject, List<Grade>> getGradesBySubject() {
        return gradesBySubject;
    }

    public void addGrade(Subject subject, double value, Date insertionDate) {
        Grade grade = new Grade(value, insertionDate);

        if (!gradesBySubject.containsKey(subject)) {
            gradesBySubject.put(subject, new ArrayList<>());
        }

        gradesBySubject.get(subject).add(grade);
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
