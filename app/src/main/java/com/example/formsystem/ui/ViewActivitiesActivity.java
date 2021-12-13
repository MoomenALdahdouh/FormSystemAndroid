package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.formsystem.R;
import com.example.formsystem.adapter.ActivitiesAdapter;
import com.example.formsystem.databinding.ActivityViewActivitiesBinding;

public class ViewActivitiesActivity extends AppCompatActivity {

    private ActivityViewActivitiesBinding binding;
    private String activityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewActivitiesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ActivitiesAdapter.ACTIVITY_ID)) {
            activityId = intent.getStringExtra(ActivitiesAdapter.ACTIVITY_ID);
            makeInterViewClick();
        }
    }

    private void makeInterViewClick() {
        binding.buttonMakeInterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewActivitiesActivity.this, MakeInterviewActivity.class);
                startActivity(intent);
            }
        });
    }
}