package com.example.formsystem.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.formsystem.client.FormSystemClient;
import com.example.formsystem.model.Activity;
import com.example.formsystem.model.ActivityResults;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.Login;
import com.example.formsystem.model.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormSystemViewModel extends ViewModel {
    public MutableLiveData<Token> tokenMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ActivityResults> activitiesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Form> formsMutableLiveData = new MutableLiveData<>();

    public void login(Login login) {
        FormSystemClient.getINSTANCE().login(login).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                // Log.d("onResponse", response.toString());
                tokenMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                t.printStackTrace();
                //Log.d("onFailure", t.getMessage());
            }
        });
    }

    public void getAllActivities(String authToken) {
        FormSystemClient.getINSTANCE().getAllActivities(authToken).enqueue(new Callback<ActivityResults>() {
            @Override
            public void onResponse(@NonNull Call<ActivityResults> call, @NonNull Response<ActivityResults> response) {
                Log.d("onResponse", response.toString());
                activitiesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ActivityResults> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("onFailure", t.getMessage());
            }
        });
    }

    public void getForms(String authToken, String id) {
        FormSystemClient.getINSTANCE().getAllForms(authToken, id).enqueue(new Callback<Form>() {
            @Override
            public void onResponse(@NonNull Call<Form> call, @NonNull Response<Form> response) {
                // Log.d("onResponse", response.toString());
                formsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Form> call, @NonNull Throwable t) {
                t.printStackTrace();
                //Log.d("onFailure", t.getMessage());
            }
        });
    }
}
