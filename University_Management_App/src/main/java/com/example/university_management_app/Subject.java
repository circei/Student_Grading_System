package com.example.university_management_app;

public class Subject {
    private String subjectName;
    private String teacher;
    private int credits;

    public Subject(String subjectName, String teacher, int credits) {
        this.subjectName = subjectName;
        this.teacher = teacher;
        this.credits = credits;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
