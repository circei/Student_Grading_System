import java.util.*;
public class Subject {
    private final String name;
    private List<Grade> grades;
    public Subject(String name){
        this.name = name;
        this.grades = new ArrayList<>();
    }
    public String getName() {
        return name;
    }
    public List<Grade> getGrades(){
        return grades;
    }
    public void addGrade(double value, Date inserationDate){
        Grade grade = new Grade(value, inserationDate);
        grades.add(grade);
    }
    public double finalGrade(){
        if(grades.isEmpty()){
            return 0.0;
        }
        double sum = 0;
        for (Grade grade : grades){
            sum += grade.getValue();
        }
        return sum/grades.size();
    }



}
