package com.example.formsystem.client;

import com.example.formsystem.interfaces.FormSystemInterface;
import com.example.formsystem.model.Form;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormSystemClient {
    public static final String BASE_URL = "http://127.0.0.1:8000/api/";
    private static FormSystemClient INSTANCE;
    private final FormSystemInterface formSystemInterface;

    public FormSystemClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        formSystemInterface = retrofit.create(FormSystemInterface.class);

    }

    public static FormSystemClient getINSTANCE() {
        if (null == INSTANCE) {
            INSTANCE = new FormSystemClient();
        }
        return INSTANCE;
    }

    public Call<Form> getAllForms(String id) {
        return formSystemInterface.getAllForms(id);
    }
}
