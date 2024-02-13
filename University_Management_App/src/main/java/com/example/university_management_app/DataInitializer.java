package com.example.university_management_app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DataInitializer {
    private Connection connection;
    public DataInitializer(){
        try{
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","1q2w3e");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean teacherHasSubject(String teacherName, String subjectName) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM teacher_subjects WHERE teacher_name = ? AND subject_name = ?");
            statement.setString(1, teacherName);
            statement.setString(2, subjectName);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Returns true if there is a row matching the query
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static List<Subject> allSubjects = new ArrayList<>();

    private static Subject findSubjectByName(String subjectName) {

        for (Subject existingSubject : allSubjects) {
            if (existingSubject.getName().equals(subjectName)) {
                return existingSubject;
            }
        }
        return null;
    }

    public static List<Subject> getAllSubjects() {
        return allSubjects;
    }


}
