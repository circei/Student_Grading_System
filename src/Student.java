import java.util.*;
public class Student {
    private String name;
    private Map <String,Subject> subjects;

    public Student (String name){
        this.name = name;
        this.subjects = new HashMap<>();
    }
    public String getName(){
        return name;
    }
    public Map<String,Subject> getSubjects(){
        return subjects;
    }
}
