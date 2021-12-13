package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.formsystem.R;
import com.example.formsystem.databinding.ActivityViewActivitiesBinding;

public class ViewActivitiesActivity extends AppCompatActivity {

    private ActivityViewActivitiesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewActivitiesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}