package com.example.university_management_app;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class Student implements User {
    private String name;

    private Map<Subject, List<Grade>> gradesBySubject;

    public Student(String name) {
        this.name = name;
        this.gradesBySubject = new HashMap<>();
    }
    public void initializeGradesFromList(){
        File file = new File("grades_" + getName() + ".txt");
        if (file.exists()) {

        }
    }
    public String getName() {
        return name;
    }

    public Map<Subject, List<Grade>> getGradesBySubject() {
        return gradesBySubject;
    }






    @Override
    public String getUsername() {
        return name;
    }
    @Override
    public String getRole() {
        return "student";
    }










    private Date parseDate(String dateLine) {
        Date gradeDate = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            gradeDate = dateFormat.parse(dateLine.substring("Date: ".length()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gradeDate;
    }
}
