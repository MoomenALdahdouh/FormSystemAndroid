package com.example.formsystem.interfaces;

import com.example.formsystem.model.Form;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FormSystemInterface {

    @GET("forms/{id}")
    Call<Form> getAllForms(@Path("id") String id);

   /* @GET("movie/{movie_id}")
    Call<DetailMovie> getDetailMovieById(@Path("movie_id") String movie_id, @Query("api_key") String api_key);*/

    /*@GET("movie/upcoming")
    Call<MoviesModel> getLatestTrailer(@Query("api_key") String api_key, @Query("page") String page, @Query("sort_by") String release_date);

    @GET("movie/popular")
    Call<MoviesModel> getMoviesPopular(@Query("api_key") String api_key, @Query("page") String page);

    @GET("movie/top_rated")
    Call<MoviesModel> getMoviesTopRated(@Query("api_key") String api_key, @Query("page") String page);*/

}
