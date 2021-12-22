package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.example.formsystem.viewmodel.local.ActivitiesViewModel;
import com.example.formsystem.viewmodel.local.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class ActivitiesActivity extends AppCompatActivity {

    private ActivityActivitiesBinding binding;
    private FormSystemViewModel activitiesSystemViewModel;
    private FormSystemViewModel userSystemViewModel;
    private UserViewModel userViewModel;
    private ActivitiesViewModel activitiesViewModel;
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
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        activitiesViewModel = new ViewModelProvider(this).get(ActivitiesViewModel.class);
        recyclerView = binding.recyclerView;
        activitiesAdapter = new ActivitiesAdapter(ActivitiesActivity.this);
        activityArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activitiesAdapter.setList(activityArrayList);
        recyclerView.setAdapter(activitiesAdapter);
        recyclerView.setHasFixedSize(true);
        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
        binding.loadingDataConstraint.setVisibility(View.GONE);
        if (isNetworkAvailable()) {
            getUserDetailsNet();
            getActivitiesNet();
        } else {
            getUserDetailsNoNet();
            getActivitiesNoNet();
        }
        //getActivities();
        //getUserDetailsNet();
        setUpLanguage(PreferenceUtils.getLanguage(getApplicationContext()));
        // checkInternet();
    }

    private void getUserDetailsNet() {
        userSystemViewModel.getUser(token, userId);
        userSystemViewModel.userMutableLiveData.observe(this, new Observer<UserResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(UserResults userResults) {
                User user = userResults.getUser();
                //Delete old user in local
                userViewModel.delete(user);
                //Save new user in local
                userViewModel.insert(user);
                Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();
                //view user details
                try {
                    binding.textViewUserName.setText(user.getName());
                    binding.textViewUserEmail.setText(user.getEmail());
                } catch (Exception e) {

                }

            }
        });
    }

    private void getUserDetailsNoNet() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                User user = users.get(0);
                binding.textViewUserName.setText(user.getName());
                binding.textViewUserEmail.setText(user.getEmail());
                Toast.makeText(getApplicationContext(), "Local", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getActivitiesNet() {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        activitiesSystemViewModel.getAllActivities(token, userId);
        activitiesSystemViewModel.activitiesMutableLiveData.observe(this, new Observer<ActivityResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ActivityResults activityResults) {
                binding.loadingDataConstraint.setVisibility(View.GONE);
                activityArrayList = activityResults.getResults();
                activitiesViewModel.deleteAllActivities();
                for (int i = 0; i < activityArrayList.size(); i++) {
                    activitiesViewModel.insert(activityArrayList.get(i));
                }
                if (!activityArrayList.isEmpty()) {
                    binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                    activitiesAdapter.setList(activityArrayList);
                    activitiesAdapter.notifyDataSetChanged();
                } else
                    binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getActivitiesNoNet() {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        activitiesSystemViewModel.getAllActivities(token, userId);
        activitiesViewModel.getAllActivities().observe(this, new Observer<List<Activity>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(@Nullable List<Activity> activities) {
                binding.loadingDataConstraint.setVisibility(View.GONE);
                assert activities != null;
                if (!activities.isEmpty()) {
                    binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                    activityArrayList.addAll(activities);
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
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(language));
        resources.updateConfiguration(configuration, displayMetrics);
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}