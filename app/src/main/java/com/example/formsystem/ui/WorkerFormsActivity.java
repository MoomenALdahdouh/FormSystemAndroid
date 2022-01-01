package com.example.formsystem.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formsystem.R;
import com.example.formsystem.adapter.FormsAdapter;
import com.example.formsystem.databinding.ActivityActivitiesBinding;
import com.example.formsystem.databinding.ActivityWorkerFormsBinding;
import com.example.formsystem.model.Activity;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.FormResults;
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

public class WorkerFormsActivity extends AppCompatActivity {

    private ActivityWorkerFormsBinding binding;
    private FormSystemViewModel activitiesSystemViewModel;
    private FormSystemViewModel workerFormsSystemViewModel;
    private FormSystemViewModel userSystemViewModel;
    private UserViewModel userViewModel;
    private ActivitiesViewModel activitiesViewModel;
    private RecyclerView recyclerView;
    private FormsAdapter formsAdapter;
    private ArrayList<Form> formsArrayList;
    private String token;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkerFormsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        token = PreferenceUtils.getToken(WorkerFormsActivity.this);
        userId = PreferenceUtils.getUserId(WorkerFormsActivity.this);
        activitiesSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        workerFormsSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        userSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        activitiesViewModel = new ViewModelProvider(this).get(ActivitiesViewModel.class);
        recyclerView = binding.recyclerView;
        formsAdapter = new FormsAdapter(WorkerFormsActivity.this);
        formsArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        formsAdapter.setList(formsArrayList);
        recyclerView.setAdapter(formsAdapter);
        recyclerView.setHasFixedSize(true);
        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
        binding.loadingDataConstraint.setVisibility(View.GONE);
        if (isNetworkAvailable()) {
            getUserDetailsNet();
            getFormsNet();
            //getActivitiesNet();
        } else {
            getUserDetailsNoNet();
            //getActivitiesNoNet();
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


    private void getFormsNet() {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        workerFormsSystemViewModel.getAllWorkerForms(token, userId);
        workerFormsSystemViewModel.workerFormsMutableLiveData.observe(this, new Observer<FormResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(FormResults formResults) {
                binding.loadingDataConstraint.setVisibility(View.GONE);
                formsArrayList = formResults.getResults();
                //Store data in room
                /*activitiesViewModel.deleteAllActivities();
                for (int i = 0; i < formsArrayList.size(); i++) {
                    activitiesViewModel.insert(formsArrayList.get(i));
                }*/
                if (!formsArrayList.isEmpty()) {
                    binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                    formsAdapter.setList(formsArrayList);
                    formsAdapter.notifyDataSetChanged();
                } else
                    binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
            }
        });
    }


    /*private void getActivitiesNoNet() {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        activitiesViewModel.getAllActivities().observe(this, new Observer<List<Activity>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(@Nullable List<Activity> activities) {
                binding.loadingDataConstraint.setVisibility(View.GONE);
                assert activities != null;
                if (!activities.isEmpty()) {
                    binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                    formsArrayList.addAll(activities);
                    formsAdapter.setList(formsArrayList);
                    formsAdapter.notifyDataSetChanged();
                } else
                    binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
            }
        });
    }*/

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
        startActivity(new Intent(getApplicationContext(), WorkerFormsActivity.class));
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