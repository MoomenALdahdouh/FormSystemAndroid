package com.example.formsystem.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.formsystem.client.FormSystemClient;
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
import com.example.formsystem.model.Questions;
import com.example.formsystem.model.QuestionsResults;
import com.example.formsystem.model.ResponseSuccess;
import com.example.formsystem.model.Token;
import com.example.formsystem.model.User;
import com.example.formsystem.model.UserResults;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormSystemViewModel extends ViewModel {
    public MutableLiveData<Token> tokenMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ActivityResults> activitiesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<FormResults> workerFormsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<UserResults> userMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<FormResults> formMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<InterviewResults> interviewsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Interview> postInterviewMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<InterviewResults> postInterviewsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Interview> updateInterviewMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<PostAnswersList> postAnswerMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<PostAnswersList> updateAnswerMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ResponseSuccess> deleteInterviewMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<QuestionsResults> questionsMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<AnswersResults> answersMutableLiveData = new MutableLiveData<>();
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

    public void getAllActivities(String authToken, String id) {
        FormSystemClient.getINSTANCE().getAllActivities(authToken, id).enqueue(new Callback<ActivityResults>() {
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

    public void getAllWorkerForms(String authToken, String id) {
        FormSystemClient.getINSTANCE().getAllWorkerForms(authToken, id).enqueue(new Callback<FormResults>() {
            @Override
            public void onResponse(@NonNull Call<FormResults> call, @NonNull Response<FormResults> response) {
                Log.d("onResponse", response.toString());
                workerFormsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<FormResults> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("onFailure", t.getMessage());
            }
        });
    }

    public void getUser(String authToken, String id) {
        FormSystemClient.getINSTANCE().getUser(authToken, id).enqueue(new Callback<UserResults>() {
            @Override
            public void onResponse(@NonNull Call<UserResults> call, @NonNull Response<UserResults> response) {
                Log.d("onResponse", response.toString());
                userMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<UserResults> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("onFailure", t.getMessage());
            }
        });
    }

    public void getForm(String authToken, String id) {
        FormSystemClient.getINSTANCE().getForm(authToken, id).enqueue(new Callback<FormResults>() {
            @Override
            public void onResponse(@NonNull Call<FormResults> call, @NonNull Response<FormResults> response) {
                Log.d("onResponse", response.toString());
                formMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<FormResults> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("onFailure", t.getMessage());
            }
        });
    }

    public void getInterviews(String authToken, String id, String worker_id) {
        FormSystemClient.getINSTANCE().getInterviews(authToken, id, worker_id).enqueue(new Callback<InterviewResults>() {
            @Override
            public void onResponse(@NonNull Call<InterviewResults> call, @NonNull Response<InterviewResults> response) {
                Log.d("onResponse", response.toString());
                interviewsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<InterviewResults> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("onFailure", t.getMessage());
            }
        });
    }

    public void postInterview(Interview interview) {
        FormSystemClient.getINSTANCE().postInterview(interview).enqueue(new Callback<Interview>() {
            @Override
            public void onResponse(@NonNull Call<Interview> call, @NonNull Response<Interview> response) {
                Log.d("onResponse", response.toString());
                postInterviewMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Interview> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("onFailure", t.getMessage());
            }
        });
    }

    public void postInterviews(InterviewResults interviewResults) {
        FormSystemClient.getINSTANCE().postInterviews(interviewResults).enqueue(new Callback<InterviewResults>() {
            @Override
            public void onResponse(@NonNull Call<InterviewResults> call, @NonNull Response<InterviewResults> response) {
                Log.d("onResponse", response.toString());
                postInterviewsMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<InterviewResults> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("onFailure", t.getMessage());
            }
        });
    }

    public void updateInterview(Interview interview) {
        FormSystemClient.getINSTANCE().updateInterview(interview).enqueue(new Callback<Interview>() {
            @Override
            public void onResponse(@NonNull Call<Interview> call, @NonNull Response<Interview> response) {
                Log.d("onResponse", response.toString());
                updateInterviewMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Interview> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("onFailure", t.getMessage());
            }
        });
    }


    public void getQuestions(String authToken, String id) {
        FormSystemClient.getINSTANCE().getQuestions(authToken, id).enqueue(new Callback<QuestionsResults>() {
            @Override
            public void onResponse(@NonNull Call<QuestionsResults> call, @NonNull Response<QuestionsResults> response) {
                Log.d("onResponse", response.toString());
                questionsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<QuestionsResults> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("onFailure", t.getMessage());
            }
        });
    }


    public void getAnswers(String authToken, String id) {
        FormSystemClient.getINSTANCE().getAnswers(authToken, id).enqueue(new Callback<AnswersResults>() {
            @Override
            public void onResponse(@NonNull Call<AnswersResults> call, @NonNull Response<AnswersResults> response) {
                Log.d("onResponse", response.toString());
                answersMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<AnswersResults> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("onFailure", t.getMessage());
            }
        });
    }


    public void postAnswer(PostAnswersList answer) {
        FormSystemClient.getINSTANCE().postAnswer(answer).enqueue(new Callback<PostAnswersList>() {
            @Override
            public void onResponse(@NonNull Call<PostAnswersList> call, @NonNull Response<PostAnswersList> response) {
                Log.d("onResponse", response.toString());
                postAnswerMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<PostAnswersList> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("onFailure", t.getMessage());
            }
        });
    }


    public void updateAnswers(PostAnswersList answer) {
        FormSystemClient.getINSTANCE().updateAnswers(answer).enqueue(new Callback<PostAnswersList>() {
            @Override
            public void onResponse(@NonNull Call<PostAnswersList> call, @NonNull Response<PostAnswersList> response) {
                Log.d("onResponse", response.toString());
                updateAnswerMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<PostAnswersList> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d("onFailure", t.getMessage());
            }
        });
    }

    public void deleteInterview(int id) {
        FormSystemClient.getINSTANCE().deleteInterview(id).enqueue(new Callback<ResponseSuccess>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSuccess> call, @NonNull Response<ResponseSuccess> response) {
                Log.d("onResponse", response.toString());
                deleteInterviewMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseSuccess> call, @NonNull Throwable t) {
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
