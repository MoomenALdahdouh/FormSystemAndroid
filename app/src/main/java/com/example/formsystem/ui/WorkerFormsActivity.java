package com.example.formsystem.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formsystem.R;
import com.example.formsystem.adapter.FormsAdapter;
import com.example.formsystem.databinding.ActivityActivitiesBinding;
import com.example.formsystem.databinding.ActivityWorkerFormsBinding;
import com.example.formsystem.model.Activity;
import com.example.formsystem.model.Answer;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.FormResults;
import com.example.formsystem.model.Interview;
import com.example.formsystem.model.InterviewResults;
import com.example.formsystem.model.PostAnswersList;
import com.example.formsystem.model.ResponseSuccess;
import com.example.formsystem.model.User;
import com.example.formsystem.model.UserResults;
import com.example.formsystem.utils.PreferenceUtils;
import com.example.formsystem.viewmodel.FormSystemViewModel;
import com.example.formsystem.viewmodel.local.ActivitiesViewModel;
import com.example.formsystem.viewmodel.local.AnswersViewModel;
import com.example.formsystem.viewmodel.local.FormViewModel;
import com.example.formsystem.viewmodel.local.InterviewsViewModel;
import com.example.formsystem.viewmodel.local.UserViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class WorkerFormsActivity extends AppCompatActivity {

    private ActivityWorkerFormsBinding binding;
    private FormSystemViewModel formSystemViewModel;
    /*private FormSystemViewModel activitiesSystemViewModel;
    private FormSystemViewModel workerFormsSystemViewModel;
    private FormSystemViewModel userSystemViewModel;
    private FormSystemViewModel postInterviewSystemViewModel;
    private FormSystemViewModel postAnswerSystemViewModel;
    private FormSystemViewModel deleteInterviewSystemViewModel;*/
    private AnswersViewModel answersViewModel;
    private InterviewsViewModel interviewsViewModel;
    private FormViewModel formViewModel;
    private UserViewModel userViewModel;
    private ActivitiesViewModel activitiesViewModel;
    private RecyclerView recyclerView;
    private FormsAdapter formsAdapter;
    private ArrayList<Form> formsArrayList;
    private ArrayList<Interview> interviewArrayList;
    private ArrayList<Answer> answersArrayList;
    private ArrayList<Answer> answersUpdatedArrayList;
    private ArrayList<Interview> interviewDeleted;
    private String token;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkerFormsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        token = PreferenceUtils.getToken(WorkerFormsActivity.this);
        userId = PreferenceUtils.getUserId(WorkerFormsActivity.this);
        formSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        // activitiesSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        // workerFormsSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        // userSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        //postInterviewSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        //postAnswerSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        //deleteInterviewSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        answersViewModel = new ViewModelProvider(this).get(AnswersViewModel.class);
        interviewsViewModel = new ViewModelProvider(this).get(InterviewsViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        formViewModel = new ViewModelProvider(this).get(FormViewModel.class);
        activitiesViewModel = new ViewModelProvider(this).get(ActivitiesViewModel.class);
        recyclerView = binding.recyclerView;
        formsAdapter = new FormsAdapter(WorkerFormsActivity.this);
        formsArrayList = new ArrayList<>();
        interviewDeleted = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        formsAdapter.setList(formsArrayList);
        recyclerView.setAdapter(formsAdapter);
        recyclerView.setHasFixedSize(true);
        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
        binding.loadingDataConstraint.setVisibility(View.GONE);
        if (isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "Sync data is Running...", Toast.LENGTH_SHORT).show();
            getUserDetailsNet();
            getFormsNet();
            getAnswersInRoomWasCreatedLocal();
            getInterviewInRoomWasCreatedLocal();
            loadInterviewDeletedFromLocal();
        } else {
            getUserDetailsNoNet();
            getFormsNoNet();
        }
        setUpLanguage(PreferenceUtils.getLanguage(getApplicationContext()));
    }

    private void loadInterviewDeletedFromLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Interview>>() {
        }.getType();
        interviewDeleted = gson.fromJson(json, type);
        if (interviewDeleted == null) {
            interviewDeleted = new ArrayList<>();
        }
        //Delete interview post api
        for (int i = 0; i < interviewDeleted.size(); i++) {
            Log.d("savedInterviewDeleted", i + "::" + interviewDeleted.get(i).getId());
            deleteInterview(interviewDeleted.get(i).getId(), i);
        }
    }

    private void deleteInterview(int interviewId, int i) {
        formSystemViewModel.deleteInterview(interviewId);
        formSystemViewModel.deleteInterviewMutableLiveData.observe(this, new Observer<ResponseSuccess>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ResponseSuccess response) {
                Log.d("interviewDeleted", i + "::");
                if (i < interviewDeleted.size()) {
                    interviewDeleted.remove(i);
                }
                if (i == interviewDeleted.size() - 1)
                    saveInterviewDeletedInLocal();
            }
        });
    }

    private void saveInterviewDeletedInLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(interviewDeleted);
        editor.putString("task list", json);
        editor.apply();
    }

    // private ArrayList<PostAnswersList> answersResultArrayList ;
    private void getInterviewInRoomWasCreatedLocal() {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        interviewsViewModel.getAllInterviews().observe(this, new Observer<List<Interview>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(@Nullable List<Interview> interviews) {
                binding.loadingDataConstraint.setVisibility(View.GONE);
                assert interviews != null;
                interviewArrayList = new ArrayList<>();
                if (!interviews.isEmpty()) {
                    for (int i = 0; i < interviews.size(); i++) {
                        if (interviews.get(i).isCreated_in_local()) {
                            interviewArrayList.add(interviews.get(i));
                            Log.d("LocalInterview", "Local" + interviews.get(i).getTitle() + "::" + interviews.get(i).getId());
                            postLocalInterviewWasCreated(interviews.get(i));
                        } else if (interviews.get(i).isUpdate_in_local()) {
                            postUpdateInterview(interviews.get(i));
                        }
                    }
                }
            }
        });
    }

    private void getAnswersInRoomWasCreatedLocal() {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        answersViewModel.getAllAnswers().observe(this, new Observer<List<Answer>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(@Nullable List<Answer> answers) {
                binding.loadingDataConstraint.setVisibility(View.GONE);
                assert answers != null;
                if (!answers.isEmpty()) {
                    answersArrayList = new ArrayList<>();
                    answersUpdatedArrayList = new ArrayList<>();
                    for (int i = 0; i < answers.size(); i++) {
                        if (answers.get(i).isCreated_in_local())
                            answersArrayList.add(answers.get(i));
                        else if (answers.get(i).isUpdate_in_local()) {
                            answersUpdatedArrayList.add(answers.get(i));
                        }
                    }
                    PostAnswersList postAnswersList = new PostAnswersList(answersArrayList);
                    PostAnswersList postAnswersUpdateList = new PostAnswersList(answersUpdatedArrayList);
                    postLocalAnswersWasCreated(postAnswersList);
                    postUpdateAnswer(postAnswersUpdateList);
                }
            }
        });
    }

    private void postLocalAnswersWasCreated(PostAnswersList answer) {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        formSystemViewModel.postAnswer(answer);
        formSystemViewModel.postAnswerMutableLiveData.observe(this, new Observer<PostAnswersList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(PostAnswersList response) {
                binding.loadingDataConstraint.setVisibility(View.GONE);
            }
        });
    }

    private void postUpdateAnswer(PostAnswersList answer) {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        formSystemViewModel.updateAnswers(answer);
        formSystemViewModel.updateAnswerMutableLiveData.observe(this, new Observer<PostAnswersList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(PostAnswersList response) {
                binding.loadingDataConstraint.setVisibility(View.GONE);
            }
        });
    }

    private void postLocalInterviewsWasCreated() {
        InterviewResults interviewResults = new InterviewResults(interviewArrayList);
        formSystemViewModel.postInterviews(interviewResults);
        formSystemViewModel.postInterviewsMutableLiveData.observe(this, new Observer<InterviewResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(InterviewResults interviews) {
                try {
                    /* Success*/
                    Log.d("interviewsPost", interviews.getSuccess());
                } catch (Exception e) {
                }
            }
        });
    }

    private void postLocalInterviewWasCreated(Interview interview) {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        formSystemViewModel.postInterview(interview);
        formSystemViewModel.postInterviewMutableLiveData.observe(this, new Observer<Interview>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(Interview interviews) {
                try {
                    /* Success*/
                    binding.loadingDataConstraint.setVisibility(View.GONE);
                    Log.d("interviewsPost", interviews.getInterview_id());
                } catch (Exception e) {
                }
            }
        });
    }

    private void postUpdateInterview(Interview interview) {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        formSystemViewModel.updateInterview(interview);
        formSystemViewModel.updateInterviewMutableLiveData.observe(this, new Observer<Interview>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(Interview interview) {
                binding.loadingDataConstraint.setVisibility(View.GONE);
            }
        });
    }

    private void getUserDetailsNet() {
        formSystemViewModel.getUser(token, userId);
        formSystemViewModel.userMutableLiveData.observe(this, new Observer<UserResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(UserResults userResults) {
                User user = userResults.getUser();
                //Delete old user in local
                userViewModel.delete(user);
                //Save new user in local
                userViewModel.insert(user);
                Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();
                //view user details
                try {
                    binding.textViewUserName.setText(user.getName());
                    binding.textViewUserEmail.setText(user.getEmail());
                } catch (Exception e) {

                }

            }
        });
    }

    private void getUserDetailsNoNet() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                User user = users.get(0);
                binding.textViewUserName.setText(user.getName());
                binding.textViewUserEmail.setText(user.getEmail());
                Toast.makeText(getApplicationContext(), "Local", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<Form> formsRoom = new ArrayList<>();
    private ArrayList<Form> newFormsRoom = new ArrayList<>();

    private void getFormsNet() {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        formSystemViewModel.getAllWorkerForms(token, userId);
        //getFormsRoom();
        formSystemViewModel.workerFormsMutableLiveData.observe(this, new Observer<FormResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(FormResults formResults) {
                formsArrayList = formResults.getResults();
                //Replace old data form in room
                if (newFormsRoom.size() != formsArrayList.size()) {
                    newFormsRoom = new ArrayList<>();
                    formViewModel.deleteAllForms();
                    for (int i = 0; i < formsArrayList.size(); i++) {
                        newFormsRoom.add(formsArrayList.get(i));
                        formViewModel.insert(formsArrayList.get(i));
                    }
                }
                //Fill recycle
                if (!formsArrayList.isEmpty()) {
                    binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                    formsAdapter.setList(formsArrayList);
                    formsAdapter.notifyDataSetChanged();
                } else
                    binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
                binding.loadingDataConstraint.setVisibility(View.GONE);
            }
        });
    }

    private void getFormsRoom() {
        formViewModel.deleteAllForms();
        formViewModel.getAllForms().observe(this, new Observer<List<Form>>() {
            @Override
            public void onChanged(List<Form> forms) {
                Log.d("sizeForms", forms.size() + "");
                formsRoom = new ArrayList<>();
                formsRoom.addAll(forms);
                newFormsRoom = new ArrayList<>();
                for (int i = 0; i < formsArrayList.size(); i++) {
                    newFormsRoom.add(formsArrayList.get(i));
                    Log.d("siezzzz", i + "");
                    //formViewModel.insert(formsArrayList.get(i));
                }
                Log.d("sieznewFormsRoom", newFormsRoom.size() + "");
            }
        });
    }


    private void getFormsNoNet() {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        formViewModel.getAllForms().observe(this, new Observer<List<Form>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(@Nullable List<Form> forms) {
                binding.loadingDataConstraint.setVisibility(View.GONE);
                assert forms != null;
                if (!forms.isEmpty()) {
                    binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                    formsArrayList.addAll(forms);
                    formsAdapter.setList(formsArrayList);
                    formsAdapter.notifyDataSetChanged();
                } else
                    binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                signOut();
                break;
            case R.id.action_english:
                changeLanguage("en");
                break;
            case R.id.action_spanish:
                changeLanguage("es");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        PreferenceUtils.saveEmail("", getApplicationContext());
        PreferenceUtils.saveToken("", getApplicationContext());
        PreferenceUtils.saveUserId("", getApplicationContext());
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
        finish();
    }

    private void changeLanguage(String language) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(language));
        resources.updateConfiguration(configuration, displayMetrics);
        PreferenceUtils.saveLanguage(language, getApplicationContext());
        startActivity(new Intent(getApplicationContext(), WorkerFormsActivity.class));
        finish();
    }

    private void setUpLanguage(String language) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(language));
        resources.updateConfiguration(configuration, displayMetrics);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}