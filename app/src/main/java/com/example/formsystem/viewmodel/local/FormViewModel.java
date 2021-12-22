package com.example.formsystem.viewmodel.local;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.formsystem.model.Form;
import com.example.formsystem.model.User;
import com.example.formsystem.repository.FormRepository;
import com.example.formsystem.repository.UserRepository;

import java.util.List;

public class FormViewModel extends AndroidViewModel {
    private FormRepository repository;
    private LiveData<List<Form>> allForms;

    public FormViewModel(@NonNull Application application) {
        super(application);
        repository = new FormRepository(application);
        allForms = repository.getAllForms();
    }

    public void insert(Form form) {
        repository.insert(form);
    }

    public void update(Form form) {
        repository.update(form);
    }

    public void delete(Form form) {
        repository.delete(form);
    }

    public void deleteAllForms() {
        repository.deleteAllForms();
    }

    public LiveData<List<Form>> getAllForms() {
        return allForms;
    }
}

