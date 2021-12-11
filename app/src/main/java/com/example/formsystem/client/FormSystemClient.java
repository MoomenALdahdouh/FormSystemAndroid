package com.example.formsystem.client;

import com.example.formsystem.interfaces.FormSystemInterface;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.Login;
import com.example.formsystem.model.Token;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormSystemClient {
    public static final String BASE_URL = "http://190.187.247.236/api/";//190.187.247.236  //127.0.0.1:8000
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

    public Call<Token> login(Login login) {
        return formSystemInterface.login(login);
    }

    public Call<Form> getAllForms(String authToken, String id) {
        return formSystemInterface.getAllForms(authToken, id);
    }
}
