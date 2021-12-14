package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.formsystem.databinding.ActivityViewInterviewBinding;

public class ViewInterviewActivity extends AppCompatActivity {

    private ActivityViewInterviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewInterviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}