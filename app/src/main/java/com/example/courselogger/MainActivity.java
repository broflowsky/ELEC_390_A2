package com.example.courselogger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.example.courselogger.dbConfig.TABLE_ASSIGNMENT;
import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    TextView mTextview_ass_average;
    ListView mListview_course;
    private static final String TAG = "__MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initalize floatingButton, listview, textview
        setupUI();
        loadCourses();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setupUI();
        loadCourses();
    }

    private void setupUI(){
        fab = findViewById(R.id.floating_add_course);
        mTextview_ass_average = findViewById(R.id.textview_assignment_average);
        mListview_course = findViewById(R.id.listview_main_course);

        //fab onClickListener
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG, "add course button clicked");
                FragmentHelperCourse frag_add_course = new FragmentHelperCourse();
                frag_add_course.show(getSupportFragmentManager(),"InsertCourseFragment");
            }
        });
    }
    public String getAllAssignmentAverage(){
        //returns the average of ALL assignments
        dbHelper db = new dbHelper(this);
        List<Assignment> assList = db.getAllAssignments();

        float average = 0;
        for(int i =0;i<assList.size();++i)
            average += assList.get(i).getGrade();
        average = average/assList.size();

        String temp = "Average of all assignments: " + average;
        return temp;
    }
    public String getAssignmentAverageByCourse(long course_id){
        dbHelper helper = new dbHelper(this);

        //fetch assignments from database
        List<Assignment> assignments = helper.getAllAssignmentbyCourse(course_id);
        float average = 0;
        for (Assignment assignment : assignments)
            average += assignment.getGrade();
        average = average/assignments.size();
        String temp = "Assignment Average:\t\t" + average;
        return temp;
    }

    public void loadCourses()
    {
        dbHelper helper = new dbHelper(this);
        final List<Course> courses = helper.getAllCourses();
        ArrayList<String> courseList = new ArrayList<>();//used to inflate listview

        //Get courses average, title and code
        for(int i = 0 ; i<courses.size();++i){


            //Make the string that will be put into the listview
            String temp = courses.get(i).getTitle()
                    + '\n' + courses.get(i).getCode()
                    + "\n\n" + getAssignmentAverageByCourse(courses.get(i).getId());
            courseList.add(temp);

        }
        //push to listview
        final ArrayAdapter<String> adapter =new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, courseList);

        mListview_course.setAdapter(adapter);
        mTextview_ass_average.setText(getAllAssignmentAverage());


        mListview_course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String message = adapter.getItem(position);
                if(message !=null) {
                    Log.d(TAG, message);
                    Intent intent = new Intent(view.getContext(), ShowCourseActivity.class);

                    intent.putExtra("course_id",courses.get(position).getId());
                    Log.d(TAG,valueOf(position));

                   // intent.putExtra("course_name",mListview_course.getItemAtPosition(position).toString());
                    startActivity(intent);
                }
                else Log.d(TAG, "Couldnt find the item ! listview onclickListner");

            }
        });



    }
}
