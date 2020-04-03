package com.example.courselogger;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class FragmentHelperCourse extends DialogFragment {

    private static final String TAG = "__FragmentHelperCourse";

    protected EditText mCourseTitleEditText;
    protected EditText mCourseCodeEditText;
    protected Button mSaveButton;
    protected Button mCancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        //reuse same layout when adding assignments
        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        setupUI(view);
        return view;
    }


    protected void setupUI(View view)
    {
        mCourseTitleEditText = view.findViewById(R.id.editText_fragment_title);
        mCourseCodeEditText = view.findViewById(R.id.editText_fragment_value);
        mCancelButton = view.findViewById(R.id.button_fragment_cancel);
        mSaveButton = view.findViewById(R.id.button_fragment_save);


        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDialog().dismiss();

            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbHelper helper = new dbHelper(getActivity());

                String courseTitle = mCourseTitleEditText.getText().toString();
                String courseCode = mCourseCodeEditText.getText().toString();

                if(courseTitle.length() < 1 || courseCode.length() < 1)
                    Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_LONG).show();
                else {
                    helper.createCourse(new Course(courseTitle, courseCode));

                    Toast.makeText(getActivity(), "Course Saved", Toast.LENGTH_LONG).show();

                    ((MainActivity) getActivity()).loadCourses();

                getDialog().dismiss();}



            }
        });
    }
}
