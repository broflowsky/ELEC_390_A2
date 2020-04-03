package com.example.courselogger;

import java.util.Random;

public class Assignment {

    protected String mAssignmentTitle;
    protected int  mAssignmentGrade;

    public Assignment(String title, int grade){

        mAssignmentTitle = title;
        mAssignmentGrade = grade;


    }
    //setters
    public void setmAssignmentTitle(String mAssignmentTitle) {
        this.mAssignmentTitle = mAssignmentTitle;
    }

    public void setmAssignmentGrade(int mAssignmentGrade) {
        this.mAssignmentGrade = mAssignmentGrade;
    }

    //Getters
    public String getTitle(){
        return mAssignmentTitle;
    }
    public int getGrade(){
        return mAssignmentGrade;
    }
    public String getInfoString() {
        return this.mAssignmentTitle + " " + this.mAssignmentGrade + " ";
    }
}
