package com.example.formsystem.client;

import androidx.annotation.NonNull;

import com.example.formsystem.interfaces.FormSystemInterface;
import com.example.formsystem.model.Activity;
import com.example.formsystem.model.ActivityResults;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.Login;
import com.example.formsystem.model.Token;
import com.example.formsystem.model.User;
import com.example.formsystem.model.UserResults;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FormSystemClient {
    public static final String BASE_URL = "http://190.187.247.236/api/";//190.187.247.236  //127.0.0.1:8000
    private static FormSystemClient INSTANCE;
    private final FormSystemInterface formSystemInterface;

    public FormSystemClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder newRequest = request.newBuilder().header("authorization", "token");
                return chain.proceed(newRequest.build());
            }
        });

        /*OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(interceptor);*/


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
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

    public Call<ActivityResults> getAllActivities(String authToken, String id) {
        return formSystemInterface.getAllActivities("Bearer " + authToken, id);
    }

    public Call<UserResults> getUser(String authToken, String id) {
        return formSystemInterface.getUser("Bearer " + authToken, id);
    }

    public Call<Form> getAllForms(String authToken, String id) {
        return formSystemInterface.getAllForms(authToken, id);
    }
}
