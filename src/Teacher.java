import java.util.*;
public class Teacher implements User {
    private String name;
    private Map<String,Subject> subjects;
    private Set<String> permissions;
    private DataInitializer dataInitializer;
    private String password = "teacher";

    public boolean authenticate(String enteredPassword) {
        return this.password.equals(enteredPassword);
    }
    public Teacher(String name, DataInitializer dataInitializer) {
        this.name = name;
        this.subjects = new HashMap<>();
        this.permissions = new HashSet<>();
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


    public void initializePermissions() {
        // Grant CRUD permissions for each subject the teacher teaches
        subjects.keySet().forEach(subjectName -> {
            grantSubjectPermissions(subjectName);
        });
    }
    private void grantSubjectPermissions(String subjectName) {
        subjectName = subjectName.trim();
        permissions.add("CREATE_GRADE_" + subjectName);
        permissions.add("UPDATE_GRADE_" + subjectName);
        permissions.add("DELETE_GRADE_" + subjectName);
    }

    @Override
    public String getUsername() {
        // Assuming the teacher's name can be used as a username
        return name;
    }

    @Override
    public String getRole() {
        return "TEACHER";
    }


    public boolean hasSubjectPermissions(String subjectName) {
        boolean createPermission = permissions.contains("CREATE_GRADE_" + subjectName);
        boolean updatePermission = permissions.contains("UPDATE_GRADE_" + subjectName);
        boolean deletePermission = permissions.contains("DELETE_GRADE_" + subjectName);

        return createPermission && updatePermission && deletePermission;
    }

}
