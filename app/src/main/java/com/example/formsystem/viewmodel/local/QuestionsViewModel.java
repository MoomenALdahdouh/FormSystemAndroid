package com.example.formsystem.viewmodel.local;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.formsystem.model.Questions;
import com.example.formsystem.model.User;
import com.example.formsystem.repository.QuestionsRepository;
import com.example.formsystem.repository.UserRepository;

import java.util.List;

public class QuestionsViewModel extends AndroidViewModel {
    private QuestionsRepository repository;
    private LiveData<List<Questions>> allQuestions;

    public QuestionsViewModel(@NonNull Application application) {
        super(application);
        repository = new QuestionsRepository(application);
        allQuestions = repository.getAllQuestions();
    }

    public void insert(Questions questions) {
        repository.insert(questions);
    }

    public void update(Questions questions) {
        repository.update(questions);
    }

    public void delete(Questions questions) {
        repository.delete(questions);
    }

    public void deleteAllQuestions() {
        repository.deleteAllQuestions();
    }

    public LiveData<List<Questions>> getAllQuestions() {
        return allQuestions;
    }
}


