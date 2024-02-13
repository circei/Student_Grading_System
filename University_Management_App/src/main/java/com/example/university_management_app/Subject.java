package com.example.university_management_app;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Subject implements Serializable{
    private final String name;
    private final Teacher teacher;
    private List<Student> enrolledStudents;
    private List<Grade> grades;


    public Subject(String name, Teacher teacher) {
        this.name = name;
        this.teacher = teacher;
        this.enrolledStudents = new ArrayList<>();
        this.grades = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Teacher getTeacher() {
        return teacher;
    }
    public void addGrade(Student student, double value, Date insertionDate, Teacher teacher) {
        Grade grade = new Grade(value,insertionDate);
        grades.add(grade);
        student.addGrade(this, value, insertionDate, teacher);
    }



    public void enrollStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
        }
    }


    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }
    public double calculateFinalGradeForThisSubject(){
        double averageGrade = grades.stream()
                .mapToDouble(Grade::getValue)
                .average()
                .orElse(0.0);

        return averageGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(name, subject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
