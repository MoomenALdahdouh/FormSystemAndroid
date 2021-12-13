package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.formsystem.R;
import com.example.formsystem.adapter.ActivitiesAdapter;
import com.example.formsystem.databinding.ActivityViewActivitiesBinding;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.FormResults;
import com.example.formsystem.model.User;
import com.example.formsystem.model.UserResults;
import com.example.formsystem.utils.PreferenceUtils;
import com.example.formsystem.viewmodel.FormSystemViewModel;

public class ViewActivitiesActivity extends AppCompatActivity {

    public static final String FORM_ID = "FORM_ID";
    private ActivityViewActivitiesBinding binding;
    private String activityId;
    private String formId;
    private FormSystemViewModel formSystemViewModel;
    private String token;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewActivitiesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        token = PreferenceUtils.getToken(ViewActivitiesActivity.this);
        formSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        userId = PreferenceUtils.getUserId(ViewActivitiesActivity.this);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ActivitiesAdapter.ACTIVITY_ID)) {
            activityId = intent.getStringExtra(ActivitiesAdapter.ACTIVITY_ID);
            makeInterviewClick();
            getForm();
            getInterviews();
        }
    }

    private void getInterviews() {
    }

    private void getForm() {
        formSystemViewModel.getForm(token, activityId);
        formSystemViewModel.formMutableLiveData.observe(this, new Observer<FormResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(FormResults formResults) {
                Form form = formResults.getForm();
                try {
                    formId = form.getId();
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