package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import java.util.Locale;

import io.reactivex.annotations.NonNull;

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
        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
        binding.loadingDataConstraint.setVisibility(View.GONE);
        getActivities();
        getUserDetails();
        setUpLanguage(PreferenceUtils.getLanguage(getApplicationContext()));

    }

    private void getUserDetails() {
        userSystemViewModel.getUser(token, userId);
        userSystemViewModel.userMutableLiveData.observe(this, new Observer<UserResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(UserResults userResults) {
                User user = userResults.getUser();
                try {
                    binding.textViewUserName.setText(user.getName());
                    binding.textViewUserEmail.setText(user.getEmail());
                } catch (Exception e) {

                }

            }
        });
    }

    private void getActivities() {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        activitiesSystemViewModel.getAllActivities(token, userId);
        activitiesSystemViewModel.activitiesMutableLiveData.observe(this, new Observer<ActivityResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ActivityResults activityResults) {
                binding.loadingDataConstraint.setVisibility(View.GONE);
                activityArrayList = activityResults.getResults();
                if (!activityArrayList.isEmpty()) {
                    binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                    activitiesAdapter.setList(activityArrayList);
                    activitiesAdapter.notifyDataSetChanged();
                } else
                    binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                signOut();
                break;
            case R.id.action_english:
                changeLanguage("en");
                break;
            case R.id.action_spanish:
                changeLanguage("es");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        PreferenceUtils.saveEmail("", getApplicationContext());
        PreferenceUtils.saveToken("", getApplicationContext());
        PreferenceUtils.saveUserId("", getApplicationContext());
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
        finish();
    }

    private void changeLanguage(String language) {
        /*Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(language));
        resources.updateConfiguration(configuration, displayMetrics);*/
        PreferenceUtils.saveLanguage(language, getApplicationContext());
        startActivity(new Intent(getApplicationContext(), ActivitiesActivity.class));
        finish();
    }

    private void setUpLanguage(String language) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(language));
        resources.updateConfiguration(configuration, displayMetrics);
    }
}