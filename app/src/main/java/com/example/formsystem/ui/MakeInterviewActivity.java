package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.formsystem.databinding.ActivityMainBinding;
import com.example.formsystem.databinding.ActivityMakeInterviewBinding;

public class MakeInterviewActivity extends AppCompatActivity {

    private ActivityMakeInterviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeInterviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}