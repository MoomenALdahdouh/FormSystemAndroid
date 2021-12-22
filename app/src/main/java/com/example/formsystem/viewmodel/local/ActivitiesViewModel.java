package com.example.formsystem.viewmodel.local;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.formsystem.model.Activity;
import com.example.formsystem.model.User;
import com.example.formsystem.repository.ActivitiesRepository;
import com.example.formsystem.repository.UserRepository;

import java.util.List;

public class ActivitiesViewModel extends AndroidViewModel {
    private ActivitiesRepository repository;
    private LiveData<List<Activity>> allActivities;

    public ActivitiesViewModel(@NonNull Application application) {
        super(application);
        repository = new ActivitiesRepository(application);
        allActivities = repository.getAllActivities();
    }

    public void insert(Activity activity) {
        repository.insert(activity);
    }

    public void update(Activity activity) {
        repository.update(activity);
    }

    public void delete(Activity activity) {
        repository.delete(activity);
    }

    public void deleteAllActivities() {
        repository.deleteAllUsers();
    }

    public LiveData<List<Activity>> getAllActivities() {
        return allActivities;
    }
}

