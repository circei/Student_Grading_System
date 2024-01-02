import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Subject {
    private final String name;
    private final Teacher teacher;
    private List<Student> enrolledStudents;


    public Subject(String name, Teacher teacher) {
        this.name = name;
        this.teacher = teacher;
        this.enrolledStudents = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Teacher getTeacher() {
        return teacher;
    }
    public void addGrade(Student student, double value, Date insertionDate) {
        student.addGrade(this, value, insertionDate);
    }


    public void enrollStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
        }
    }


}
