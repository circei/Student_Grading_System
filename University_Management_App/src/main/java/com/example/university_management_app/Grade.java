package com.example.university_management_app;
import java.util.*;
public class Grade {
    private double value;
    private Date inserationDate;

    public Grade(double value, Date inserationDate){
        this.value = value;
        this.inserationDate = inserationDate;
    }

    public Grade() {

    }

    public double getValue(){
        return value;
    }

    public void setValue(double newValue){this.value = newValue;}

    public Date getInserationDate(){
        return inserationDate;
    }
}
