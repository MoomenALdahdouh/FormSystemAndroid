package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.example.formsystem.R;
import com.example.formsystem.adapter.ActivitiesAdapter;
import com.example.formsystem.databinding.ActivityActivitiesBinding;
import com.example.formsystem.model.Activity;
import com.example.formsystem.model.ActivityResults;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.User;
import com.example.formsystem.model.UserResults;
import com.example.formsystem.utils.PreferenceUtils;
import com.example.formsystem.viewmodel.FormSystemViewModel;

import java.util.ArrayList;

public class ActivitiesActivity extends AppCompatActivity {

    private ActivityActivitiesBinding binding;
    private FormSystemViewModel activitiesSystemViewModel;
    private FormSystemViewModel userSystemViewModel;
    private RecyclerView recyclerView;
    private ActivitiesAdapter activitiesAdapter;
    private ArrayList<Activity> activityArrayList;
    private String token;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActivitiesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        token = PreferenceUtils.getToken(ActivitiesActivity.this);
        userId = PreferenceUtils.getUserId(ActivitiesActivity.this);
        activitiesSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        userSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        recyclerView = binding.recyclerView;
        activitiesAdapter = new ActivitiesAdapter(ActivitiesActivity.this);
        activityArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activitiesAdapter.setList(activityArrayList);
        recyclerView.setAdapter(activitiesAdapter);
        recyclerView.setHasFixedSize(true);

        getActivities();
        getUserDetails();

    }

    private void getUserDetails() {
        userSystemViewModel.getUser(token, userId);
        userSystemViewModel.userMutableLiveData.observe(this, new Observer<UserResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(UserResults userResults) {
                User user = userResults.getUser();
                binding.textViewUserName.setText(user.getName());
                binding.textViewUserEmail.setText(user.getEmail());
            }
        });
    }

    private void getActivities() {
        activitiesSystemViewModel.getAllActivities(token, userId);
        activitiesSystemViewModel.activitiesMutableLiveData.observe(this, new Observer<ActivityResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ActivityResults activityResults) {
                activityArrayList = activityResults.getResults();
                //binding.textView5.setText(activityArrayList.get(0).getName());
                activitiesAdapter.setList(activityArrayList);
                activitiesAdapter.notifyDataSetChanged();
            }
        });
    }
}