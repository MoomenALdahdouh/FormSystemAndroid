package com.example.formsystem.interfaces;

import com.example.formsystem.model.Activity;
import com.example.formsystem.model.ActivityResults;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.FormResults;
import com.example.formsystem.model.Login;
import com.example.formsystem.model.Token;
import com.example.formsystem.model.User;
import com.example.formsystem.model.UserResults;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FormSystemInterface {

    @POST("login")
    Call<Token> login(@Body Login login);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @GET("activities/{id}")
    Call<ActivityResults> getAllActivities(@Header("authorization") String authToken, @Path("id") String id);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @GET("user/{id}")
    Call<UserResults> getUser(@Header("authorization") String authToken, @Path("id") String id);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @GET("activities/{id}/form")
    Call<FormResults> getForm(@Header("authorization") String authToken, @Path("id") String id);

    //@Headers("Content-Type: application/json")
    @GET("forms/{id}")
    Call<Form> getAllForms(@Header("authorization") String authToken, @Path("id") String id);

   /* @GET("movie/{movie_id}")
    Call<DetailMovie> getDetailMovieById(@Path("movie_id") String movie_id, @Query("api_key") String api_key);*/

    /*@GET("movie/upcoming")
    Call<MoviesModel> getLatestTrailer(@Query("api_key") String api_key, @Query("page") String page, @Query("sort_by") String release_date);

    @GET("movie/popular")
    Call<MoviesModel> getMoviesPopular(@Query("api_key") String api_key, @Query("page") String page);

    @GET("movie/top_rated")
    Call<MoviesModel> getMoviesTopRated(@Query("api_key") String api_key, @Query("page") String page);*/

}
