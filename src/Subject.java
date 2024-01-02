import java.util.Date;


public class Subject {
    private final String name;
    private final Teacher teacher;


    public Subject(String name, Teacher teacher) {
        this.name = name;
        this.teacher = teacher;
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


}
