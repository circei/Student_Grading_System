package com.example.university_management_app;

public class Grade {
    private String date;
    private String subject;
    private int grade;
    private String passed;

    public Grade(String date, String subject, int grade, String passed) {
        this.date = date;
        this.subject = subject;
        this.grade = grade;
        this.passed = passed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getPassed() {
        return passed;
    }

    public void setPassed(String passed) {
        this.passed = passed;
    }
}
