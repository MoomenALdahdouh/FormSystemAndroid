package com.example.formsystem.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.formsystem.client.FormSystemClient;
import com.example.formsystem.model.Form;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormSystemViewModel  extends ViewModel {
    public MutableLiveData<Form> formsMutableLiveData = new MutableLiveData<>();

    public void getForms(String id) {
        FormSystemClient.getINSTANCE().getAllForms(id).enqueue(new Callback<Form>() {
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
