package com.example.courselogger;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.courselogger.dbConfig.COLUMN_ASSIGNMENT_GRADE;
import static com.example.courselogger.dbConfig.COLUMN_ASSIGNMENT_TITLE;
import static com.example.courselogger.dbConfig.COLUMN_COURSE_CODE;
import static com.example.courselogger.dbConfig.COLUMN_COURSE_ID;
import static com.example.courselogger.dbConfig.COLUMN_COURSE_TITLE;
import static com.example.courselogger.dbConfig.CREATE_TABLE_ASSIGNMENT;
import static com.example.courselogger.dbConfig.CREATE_TABLE_COURSE;
import static com.example.courselogger.dbConfig.DATABASE_NAME;
import static com.example.courselogger.dbConfig.TABLE_ASSIGNMENT;
import static com.example.courselogger.dbConfig.TABLE_COURSE;
import static java.lang.String.valueOf;


public class dbHelper extends SQLiteOpenHelper {

    private static final String TAG = "__DBhelper";
    private Context context = null;
    private static final int DATABASE_VERSION = 1;


    public dbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME ,null, DATABASE_VERSION );

        Log.d(TAG, "dbHelper constructor.");

        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.d(TAG,"onCreate");

        try{
            sqLiteDatabase.execSQL(CREATE_TABLE_COURSE);
            Log.d(TAG, CREATE_TABLE_COURSE);

            sqLiteDatabase.execSQL(CREATE_TABLE_ASSIGNMENT);
            Log.d(TAG, CREATE_TABLE_ASSIGNMENT);
        }catch(SQLException e){
            Log.d(TAG,"Error in onCreate database " + e.getMessage());
        }



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int _old , int _new) {
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENT);
        onCreate(sqLiteDatabase);

    }

    ///////////////////////////////////Course
    ////////////////////////////////////////////////////////////////////////
    public long createCourse(Course course)
    {
        Log.d(TAG, "attempt to insert course: " + course.getInfoString());

        SQLiteDatabase db = this.getWritableDatabase();

        long id = -1;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COURSE_TITLE, course.getTitle());
        contentValues.put(COLUMN_COURSE_CODE, course.getCode());
        //contentValues.put(COLUMN_COURSE_ID,course.getId());

        try {
            id = db.insertOrThrow(TABLE_COURSE, null, contentValues);
            Log.d(TAG, "inserted Course: " + course.getInfoString());
        } catch (SQLException e) {
            Log.d(TAG, "operation failed" + e.getMessage());
        } finally {
            db.close();
        }

        return id;

    }

    public List<Course> getAllCourses() {
        Log.d(TAG, "get all course");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_COURSE, null, null, null,
                    null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                List<Course> courseList = new ArrayList<>();
                do {
                   // int id = cursor.getInt(cursor.getColumnIndex(COLUMN_COURSE_ID));
                    String title = cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_TITLE));
                    String code = cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_CODE));
                    long id = cursor.getLong(cursor.getColumnIndex(COLUMN_COURSE_ID));
                    courseList.add(new Course(id, title, code));

                } while (cursor.moveToNext());
                return courseList;

            }
        } catch (Exception e) {
            Log.d(TAG, "Error occured in dbHelper getAllCourse " + e.getMessage());
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return Collections.emptyList();

    }
    public void DeleteCourse(long course_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d(TAG,"course id :" + course_id);

       Log.d(TAG,"db.delete course: "+
               db.delete(TABLE_COURSE, COLUMN_COURSE_ID + " = ?",
                new String[]{valueOf(course_id)}));
        Log.d(TAG,"db.delete assignments: "+
                db.delete(TABLE_ASSIGNMENT, COLUMN_COURSE_ID + " = ?",
                        new String[]{valueOf(course_id)}));

    }
    public Course getCourse(long course_id){

        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " + TABLE_COURSE + " WHERE " + COLUMN_COURSE_ID + " = "
                + course_id;

        Log.d(TAG,query);
        Cursor cursor = db.rawQuery(query,null);

        try{
            cursor.moveToFirst();
            String title = cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_TITLE));
            String code = cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_CODE));
            return new Course(course_id,title,code);

        }catch(Exception e){
            Log.d(TAG, "Course not found! " + e.getMessage());
        }finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return new Course(-1,"couldnt find your course","error");
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////Assignment
    ////////////////////////////////////////////////////////////////////////
    public long createAssignment(Assignment assignment, long courseID)
    {
        Log.d(TAG, "attempt to insert course: " + assignment.getInfoString());

        SQLiteDatabase db = this.getWritableDatabase();

        long id = -1;

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ASSIGNMENT_TITLE, assignment.getTitle());
        contentValues.put(COLUMN_ASSIGNMENT_GRADE, assignment.getGrade());
        contentValues.put(COLUMN_COURSE_ID,courseID);

        try {
            id = db.insertOrThrow(TABLE_ASSIGNMENT, null, contentValues);
            Log.d(TAG, "inserted Assignment in  try: " + assignment.getInfoString()
                                                        + '\n' + courseID);
        } catch (SQLException e) {
            Toast.makeText(context, "operation failed: " + e.getMessage(), Toast.LENGTH_LONG);
            Log.d(TAG, "error thrown"+ e.getMessage());
        } finally {
            db.close();
        }

        return id;
    }


    public List<Assignment> getAllAssignmentbyCourse(long course_id) {

//        String selectQuery = "SELECT  * FROM " + TABLE_ASSIGNMENT + " tg, " + TABLE_COURSE_ASSIGNMENT + " tt WHERE tg."
//                + COLUMN_COURSE_TITLE + " = '" + course_title + "'" + " AND tg." + KEY_ID
//                + " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
//                + "tt." + KEY_TODO_ID;

        String selectQuery = "SELECT * FROM " + TABLE_ASSIGNMENT + " WHERE " + COLUMN_COURSE_ID
                + " = " + course_id;
        //String selectQuery = "SELECT * FROM " + TABLE_ASSIGNMENT;
        Log.d(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        try {
//            cursor = db.query(selectQuery, null, null, null,
//                    null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                List<Assignment> assignmentList = new ArrayList<>();
                do {
                    int grade = cursor.getInt(cursor.getColumnIndex(COLUMN_ASSIGNMENT_GRADE));
                    String title = cursor.getString(cursor.getColumnIndex(COLUMN_ASSIGNMENT_TITLE));

                    assignmentList.add(new Assignment(title, grade));

                } while (cursor.moveToNext());
                return assignmentList;

            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return Collections.emptyList();
    }
    public List<Assignment> getAllAssignments() {
        Log.d(TAG, "Get all assignments.");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_ASSIGNMENT, null, null, null,
                    null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                List<Assignment> assList = new ArrayList<>();
                do {
                    int grade = cursor.getInt(cursor.getColumnIndex(COLUMN_ASSIGNMENT_GRADE));
                    String title = cursor.getString(cursor.getColumnIndex(COLUMN_ASSIGNMENT_TITLE));
                    assList.add(new Assignment(title, grade));

                } while (cursor.moveToNext());
                return assList;

            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return Collections.emptyList();

    }
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////


}
