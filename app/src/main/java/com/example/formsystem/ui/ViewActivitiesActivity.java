package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.formsystem.R;
import com.example.formsystem.adapter.ActivitiesAdapter;
import com.example.formsystem.adapter.InterviewsAdapter;
import com.example.formsystem.databinding.ActivityViewActivitiesBinding;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.FormResults;
import com.example.formsystem.model.Interview;
import com.example.formsystem.model.InterviewResults;
import com.example.formsystem.model.User;
import com.example.formsystem.model.UserResults;
import com.example.formsystem.utils.PreferenceUtils;
import com.example.formsystem.viewmodel.FormSystemViewModel;

import java.util.ArrayList;

public class ViewActivitiesActivity extends AppCompatActivity {

    public static final String FORM_ID = "FORM_ID";
    private ActivityViewActivitiesBinding binding;
    private String activityId;
    private String formId;
    private FormSystemViewModel formSystemViewModel;
    private FormSystemViewModel interviewsSystemViewModel;
    private String token;
    private String userId;
    private RecyclerView recyclerView;
    private InterviewsAdapter interviewsAdapter;
    private ArrayList<Interview> interviewsArrayList;
    private ArrayList<Interview> interviewArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewActivitiesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        token = PreferenceUtils.getToken(ViewActivitiesActivity.this);
        formSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        interviewsSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        userId = PreferenceUtils.getUserId(ViewActivitiesActivity.this);
        interviewArrayList = new ArrayList<>();
        recyclerView = binding.recyclerView;
        interviewsAdapter = new InterviewsAdapter(ViewActivitiesActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        interviewsAdapter.setList(interviewArrayList);
        recyclerView.setAdapter(interviewsAdapter);
        recyclerView.setHasFixedSize(true);
        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ActivitiesAdapter.ACTIVITY_ID)) {
            activityId = intent.getStringExtra(ActivitiesAdapter.ACTIVITY_ID);
            makeInterviewClick();
            getForm();

        }
    }

    private void getInterviews(String formId) {
        interviewsSystemViewModel.getInterviews(token, formId);
        interviewsSystemViewModel.interviewsMutableLiveData.observe(this, new Observer<InterviewResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(InterviewResults interviewResults) {
                try {
                    interviewArrayList = interviewResults.getInterviews();
                    if (!interviewArrayList.isEmpty()) {
                        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                        interviewsAdapter.setList(interviewArrayList);
                        interviewsAdapter.notifyDataSetChanged();
                        interviewsAdapter.setFormId(formId);
                    } else
                        binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }

            }
        });
    }

    private void getForm() {
        formSystemViewModel.getForm(token, activityId);
        formSystemViewModel.formMutableLiveData.observe(this, new Observer<FormResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(FormResults formResults) {
                try {
                    Form form = formResults.getForm();
                    formId = form.getId();
                    getInterviews(formId);
                } catch (Exception e) {

                }

            }
        });
    }

    private void makeInterviewClick() {
        binding.buttonMakeInterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewActivitiesActivity.this, MakeInterviewActivity.class);
                intent.putExtra(FORM_ID, formId);
                startActivity(intent);
            }
        });
    }
}