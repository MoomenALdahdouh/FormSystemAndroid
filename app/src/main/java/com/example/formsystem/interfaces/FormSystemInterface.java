package com.example.formsystem.interfaces;

import com.example.formsystem.model.Activity;
import com.example.formsystem.model.ActivityResults;
import com.example.formsystem.model.Answer;
import com.example.formsystem.model.AnswersResults;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.FormResults;
import com.example.formsystem.model.Interview;
import com.example.formsystem.model.InterviewResults;
import com.example.formsystem.model.Login;
import com.example.formsystem.model.PostAnswersList;
import com.example.formsystem.model.QuestionsResults;
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

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @GET("forms/{id}/interviews")
    Call<InterviewResults> getInterviews(@Header("authorization") String authToken, @Path("id") String id);

    @POST("interviews/create")
    Call<Interview> postInterview(@Body Interview interview);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @GET("forms/{id}/questions")
    Call<QuestionsResults> getQuestions(@Header("authorization") String authToken, @Path("id") String id);

    @POST("answers/create")
    Call<PostAnswersList> postAnswer(@Body PostAnswersList answer);

    @POST("answers/update")
    Call<PostAnswersList> updateAnswers(@Body PostAnswersList answer);

    @GET("interviews/{id}/answers")
    Call<AnswersResults> getAnswers(@Header("authorization") String authToken, @Path("id") String id);


    @GET("forms/{id}")
    Call<Form> getAllForms(@Header("authorization") String authToken, @Path("id") String id);


}
