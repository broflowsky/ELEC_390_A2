package com.example.courselogger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class ShowCourseActivity extends AppCompatActivity {

    TextView mTextview_course_title;
    ListView mListview_assignment;
    FloatingActionButton fab;
    Button mButton_delete;
    private static final String TAG = "__SHOWCOURSEACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_course);
        setupUI();
        loadAssignments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI();
        loadAssignments();

    }

    private void setupUI(){
        fab = findViewById(R.id.floating_add_assignment);
        mButton_delete = findViewById(R.id.button_course_delete);
        mTextview_course_title = findViewById(R.id.textview_course_title);

        dbHelper db = new dbHelper(getApplicationContext());
        long course_id = (long)getIntent().getSerializableExtra("course_id");

        String name = db.getCourse(course_id).getInfoString();
        mTextview_course_title.setText(name);

        Log.d(TAG, db.getCourse(course_id).getTitle());

        mListview_assignment = findViewById(R.id.listview_assignment);

        //button onclick

        mButton_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG, "delete button pressed");
                dbHelper db = new dbHelper(getApplicationContext());
                long course_id = (long)getIntent().getSerializableExtra("course_id");
                Log.d(TAG,valueOf(course_id));

                db.DeleteCourse(course_id);
                finish();
            }
        });



        //fab onClickListener
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG, "add assignment button clicked");
                FragmentHelperAssignment frag_add_assignment = new FragmentHelperAssignment();
                frag_add_assignment.show(getSupportFragmentManager(),"InsertCourseFragment");
            }
        });
    }
    public void loadAssignments() {
        //query database, construct string and push to listview
        dbHelper db = new dbHelper(this);
        //get the position of the course i clicked on, through intent
        long listviewElement = (long) getIntent().getSerializableExtra("course_id");

        Log.d(TAG, "Loading all assignments, listview entry #" + listviewElement);

        List<Assignment> assignments = db.getAllAssignmentbyCourse(listviewElement);
        ArrayList<String> assList = new ArrayList<>();

        for (int i = 0; i < assignments.size(); ++i) {
            String temp = assignments.get(i).getTitle()
                    + '\n' + assignments.get(i).getGrade() + " %";
            assList.add(temp);
            Log.d(TAG, temp);
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, assList);
        mListview_assignment.setAdapter(adapter);
    }


}
