package com.example.university_management_app;
import com.example.university_management_app.DataInitializer;

import java.util.Set;

public class Teacher implements User {
    private String name;
    private Set<String> permissions;
    private DataInitializer dataInitializer;
    private String password = "teacher";

    public boolean authenticate(String enteredPassword) {
        return this.password.equals(enteredPassword);
    }

    public Teacher(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    @Override
    public String getUsername() {
        return name; // Assuming the teacher's name can be used as a username
    }

    @Override
    public String getRole() {
        return "teacher";
    }

    public boolean hasSubjectPermissions(String subjectName) {
        boolean createPermission = permissions.contains("CREATE_GRADE_" + subjectName);
        boolean updatePermission = permissions.contains("UPDATE_GRADE_" + subjectName);
        boolean deletePermission = permissions.contains("DELETE_GRADE_" + subjectName);

        return createPermission && updatePermission && deletePermission;
    }

    public boolean hasSubject(String subjectName) {
        return dataInitializer.teacherHasSubject(this.name, subjectName);
    }

    public void addGrade(String subjectName, Grade grade) {
        if (hasSubject(subjectName)) {
            // Add the grade
        } else {
            System.out.println("You are not assigned to this subject.");
        }
    }

    public void updateGrade(String subjectName, Grade grade) {
        if (hasSubject(subjectName)) {
            // Update the grade
        } else {
            System.out.println("You are not assigned to this subject.");
        }
    }

    public void deleteGrade(String subjectName, Grade grade) {
        if (hasSubject(subjectName)) {
            // Delete the grade
        } else {
            System.out.println("You are not assigned to this subject.");
        }
    }
}
