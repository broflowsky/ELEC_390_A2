package com.example.courselogger;

import java.util.ArrayList;
import java.util.Random;

public class Course {
    protected static long counter = 0;
    protected long id;
    protected String title;
    protected String code;

    public Course( String title, String code) {
        //called when the course is first created
        id=counter++;
        this.title = title;
        this.code = code;
    }
    public Course(long id, String title, String code){
        //called when we read from the database
        this.id=id;
        this.title = title;
        this.code = code;
    }
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfoString() {
        return this.title + " " + this.code + " ";
    }
}
