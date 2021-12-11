package com.example.formsystem.interfaces;

import com.example.formsystem.model.Login;
import com.example.formsystem.model.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserInterface {
    @POST("login")
    Call<Token> login(@Body Login login);
}
