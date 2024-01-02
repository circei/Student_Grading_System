import java.util.*;
public class Teacher {
    private String name;
    private Map<String,Subject> subjects;
    private DataInitializer dataInitializer;
    public Teacher(String name, DataInitializer dataInitializer) {
        this.name = name;
        this.subjects = new HashMap<>();
        this.dataInitializer = dataInitializer;
    }
    public String getName(){
        return name;
    }
    public Map<String,Subject> getSubjects(){
        return subjects;
    }
    public void addSubject(String subjectName, Subject subject) {
        // Check if the subject is already associated with this teacher
        if (!subjects.containsValue(subject)) {
            subjects.put(subjectName, subject);
        }
    }
    public void removeSubject(String subjectName){
        subjects.remove(subjectName);
    }

}
