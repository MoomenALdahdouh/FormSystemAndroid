package com.example.formsystem.viewmodel.local;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.formsystem.model.Answer;
import com.example.formsystem.repository.AnswersRepository;

import java.util.List;

public class AnswersViewModel extends AndroidViewModel {
    private AnswersRepository repository;
    private LiveData<List<Answer>> allAnswers;

    public AnswersViewModel(@NonNull Application application) {
        super(application);
        repository = new AnswersRepository(application);
        allAnswers = repository.getAllAnswers();
    }

    public void insert(Answer answer) {
        repository.insert(answer);
    }

    public void update(Answer answer) {
        repository.update(answer);
    }

    public void delete(Answer answer) {
        repository.delete(answer);
    }

    public void deleteAllAnswers() {
        repository.deleteAllAnswers();
    }

    public LiveData<List<Answer>> getAllAnswers() {
        return allAnswers;
    }
}


