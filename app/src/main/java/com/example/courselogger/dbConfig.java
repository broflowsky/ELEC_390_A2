package com.example.courselogger;

public class dbConfig {
    public static final String DATABASE_NAME = "courses-db";

    //Course table
    public static final String TABLE_COURSE = "course";
    public static final String COLUMN_COURSE_ID = "_id_course";
    public static final String COLUMN_COURSE_TITLE = "title";
    public static final String COLUMN_COURSE_CODE = "code";
    //

    //Assignment Table
    public static final String TABLE_ASSIGNMENT = "assignment";
    public static final String COLUMN_ASSIGNMENT_ID = "_id_ass";
    public static final String COLUMN_ASSIGNMENT_TITLE = "title";
    public static final String COLUMN_ASSIGNMENT_GRADE = "code";
    //




    //Create function sql string
    public static String CREATE_TABLE_COURSE = "CREATE TABLE " + TABLE_COURSE + "(" +
            COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_COURSE_TITLE + " TEXT NOT NULL, " +
            COLUMN_COURSE_CODE + " TEXT NOT NULL )";

    public static String CREATE_TABLE_ASSIGNMENT = "CREATE TABLE " + TABLE_ASSIGNMENT + "(" +
            COLUMN_ASSIGNMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_COURSE_ID + " INTEGER," +
            COLUMN_ASSIGNMENT_TITLE + " TEXT NOT NULL," +
            COLUMN_ASSIGNMENT_GRADE + " INTEGER )";


}
