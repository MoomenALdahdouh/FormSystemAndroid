package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.formsystem.adapter.ActivitiesAdapter;
import com.example.formsystem.adapter.InterviewsAdapter;
import com.example.formsystem.databinding.ActivityMainBinding;
import com.example.formsystem.databinding.ActivityMakeInterviewBinding;
import com.example.formsystem.model.Interview;

import java.util.ArrayList;

public class MakeInterviewActivity extends AppCompatActivity {

    private ActivityMakeInterviewBinding binding;
    private String formId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeInterviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ViewActivitiesActivity.FORM_ID)) {
            formId = intent.getStringExtra(ViewActivitiesActivity.FORM_ID);
            binding.textView9.setText(formId);
        }
    }
}