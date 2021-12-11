package com.example.formsystem.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.formsystem.client.FormSystemClient;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.Login;
import com.example.formsystem.model.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {
    public MutableLiveData<Token> tokenMutableLiveData = new MutableLiveData<>();

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
}
