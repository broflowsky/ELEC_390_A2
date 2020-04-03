package com.example.courselogger;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FragmentHelperAssignment extends DialogFragment {

    private static final String TAG = "__FragmentHelperAssignment";
    protected EditText mAssignmentTitleEditText;
    protected EditText mAssignmentGradeEditText;
    protected Button mSaveButton;
    protected Button mCancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        setupUI(view);
        return view;
    }

    protected void setupUI(View view)
    {
        mAssignmentTitleEditText = view.findViewById(R.id.editText_fragment_title);
        mAssignmentGradeEditText = view.findViewById(R.id.editText_fragment_value);
        mCancelButton = view.findViewById(R.id.button_fragment_cancel);
        mSaveButton = view.findViewById(R.id.button_fragment_save);

        //only allow integer
        mAssignmentGradeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

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

                String assignmentTitle = mAssignmentTitleEditText.getText().toString();

                int assignmentGrade = -1 ;

                if( mAssignmentGradeEditText.getText().toString().length() > 0) {

                    assignmentGrade = Integer.parseInt(mAssignmentGradeEditText.getText().toString());


                    if (assignmentTitle.length() > 1 && assignmentGrade <= 100){

                        //to relate assignment to course, i recover the course id from listview
                        //passed through intent, and passed here as parameter to createAssignment
                        long course_id = (long)getActivity().getIntent()
                                .getSerializableExtra("course_id");

                        helper.createAssignment(new Assignment(assignmentTitle, assignmentGrade),
                                course_id);
                        Log.d(TAG, "Ass saved:\n"
                                + assignmentTitle + '\n'
                                + assignmentGrade + '\n'
                                + course_id);
                        Toast.makeText(getActivity(), "Assignment Saved", Toast.LENGTH_LONG).show();


                        ((ShowCourseActivity) getActivity()).loadAssignments();
                        getDialog().dismiss();
                    }
                    else
                        Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_LONG).show();
            }
        });
    }
}

