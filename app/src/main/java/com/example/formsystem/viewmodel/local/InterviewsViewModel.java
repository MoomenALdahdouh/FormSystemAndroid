package com.example.formsystem.viewmodel.local;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.formsystem.model.Interview;
import com.example.formsystem.model.User;
import com.example.formsystem.repository.InterviewsRepository;
import com.example.formsystem.repository.UserRepository;

import java.util.List;

public class InterviewsViewModel extends AndroidViewModel {
    private InterviewsRepository repository;
    private LiveData<List<Interview>> allInterviews;

    public InterviewsViewModel(@NonNull Application application) {
        super(application);
        repository = new InterviewsRepository(application);
        allInterviews = repository.getAllInterviews();
    }

    public void insert(Interview interview) {
        repository.insert(interview);
    }

    public void update(Interview interview) {
        repository.update(interview);
    }

    public void delete(Interview interview) {
        repository.delete(interview);
    }

    public void deleteAllUsers() {
        repository.deleteAllInterviews();
    }

    public LiveData<List<Interview>> getAllUsers() {
        return allInterviews;
    }
}

