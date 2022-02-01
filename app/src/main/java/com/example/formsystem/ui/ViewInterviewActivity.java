package com.example.formsystem.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.formsystem.R;
import com.example.formsystem.adapter.InterviewsAdapter;
import com.example.formsystem.adapter.QuestionsAdapter;
import com.example.formsystem.databinding.ActivityViewInterviewBinding;
import com.example.formsystem.model.Answer;
import com.example.formsystem.model.AnswersResults;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.Interview;
import com.example.formsystem.model.PostAnswersList;
import com.example.formsystem.model.Questions;
import com.example.formsystem.model.QuestionsResults;
import com.example.formsystem.model.ResponseSuccess;
import com.example.formsystem.utils.PreferenceUtils;
import com.example.formsystem.viewmodel.FormSystemViewModel;
import com.example.formsystem.viewmodel.local.AnswersViewModel;
import com.example.formsystem.viewmodel.local.InterviewsViewModel;
import com.example.formsystem.viewmodel.local.QuestionsViewModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import id.zelory.compressor.Compressor;
import io.reactivex.annotations.Nullable;

public class ViewInterviewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityViewInterviewBinding binding;
    private FormSystemViewModel questionsSystemViewModel;
    private FormSystemViewModel answersSystemViewModel;
    private FormSystemViewModel postInterviewSystemViewModel;
    private FormSystemViewModel updateInterviewSystemViewModel;
    private FormSystemViewModel postAnswerSystemViewModel;
    private FormSystemViewModel updateAnswerSystemViewModel;
    private FormSystemViewModel deleteInterviewSystemViewModel;
    private String token;
    private String userId;
    private RecyclerView recyclerView;
    private QuestionsAdapter questionsAdapter;
    private ArrayList<Questions> questionsArrayList;
    private ArrayList<Answer> answersFromDbArrayList;
    private ArrayList<Questions> questionAnswersArrayList;
    private ArrayList<Answer> answersArrayList;
    private ImageView imageView;
    private TextView textView;
    private String interviewTitle = "";
    private String interviewLocation = "";
    private String interviewCreatedAt = "";
    private String formId;
    private String interviewId = "";
    private double latitude;
    private double longitude;
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    private String provider;
    private Location location;
    private GoogleMap googleMap;
    private View viewMap;

    public String questionId;
    private ArrayList<Answer> imageAnswersList = new ArrayList<>();
    private String imageName;
    private Bitmap compressor;
    private String downloadUri = null;
    private Uri imageUri = null;
    private static final int MAX_LENGTH = 100;
    private String imageUrl = "";

    private QuestionsViewModel questionsViewModel;
    private AnswersViewModel answersViewModel;
    private InterviewsViewModel interviewsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewInterviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        token = PreferenceUtils.getToken(ViewInterviewActivity.this);
        userId = PreferenceUtils.getToken(ViewInterviewActivity.this);
        questionsSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        answersSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        postInterviewSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        updateInterviewSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        postAnswerSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        updateAnswerSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        deleteInterviewSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        questionsViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
        answersViewModel = new ViewModelProvider(this).get(AnswersViewModel.class);
        interviewsViewModel = new ViewModelProvider(this).get(InterviewsViewModel.class);
        recyclerView = binding.recyclerView;
        questionAnswersArrayList = new ArrayList<>();
        answersArrayList = new ArrayList<>();
        answersFromDbArrayList = new ArrayList<>();
        interviewDeleted = new ArrayList<>();
        questionsAdapter = new QuestionsAdapter(ViewInterviewActivity.this);
        questionsArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        questionsAdapter.setList(questionsArrayList);
        questionsAdapter.isUpdateInterview(true);
        recyclerView.setAdapter(questionsAdapter);
        recyclerView.setHasFixedSize(true);
        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
        binding.loadingDataConstraint.setVisibility(View.GONE);
        binding.textViewErrorLocation.setVisibility(View.GONE);
        binding.editTextInterviewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Edit interview not available!", Toast.LENGTH_LONG).show();
            }
        });
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(InterviewsAdapter.FORM_ID)) {
            try {
                formId = intent.getStringExtra(InterviewsAdapter.FORM_ID);
                Log.d("formId", "::" + formId);
                interviewId = intent.getStringExtra(InterviewsAdapter.INTERVIEW_ID);
                interviewTitle = intent.getStringExtra(InterviewsAdapter.INTERVIEW_TITLE);
                interviewLocation = intent.getStringExtra(InterviewsAdapter.INTERVIEW_LOCATION);
                interviewCreatedAt = intent.getStringExtra(InterviewsAdapter.INTERVIEW_CREATED_AT);
                latitude = Double.parseDouble(intent.getStringExtra(InterviewsAdapter.INTERVIEW_LATITUDE));
                longitude = Double.parseDouble(intent.getStringExtra(InterviewsAdapter.INTERVIEW_LONGITUDE));
                if (isNetworkAvailable()) {
                    getFormQuestions();
                } else {
                    getFormQuestionsNoNet();
                }
                deleteInterview();
                getLocation();
                updateInterview();
                fetchInterviewData();
                loadInterviewDeletedFromLocal();
            } catch (Exception e) {

            }
        }
    }

    private void getInterviewInfoNoNet() {
        interviewsViewModel.getAllInterviews().observe(this, new Observer<List<Interview>>() {
            @Override
            public void onChanged(List<Interview> interviews) {
                for (int i = 0; i < interviews.size(); i++) {
                    if (interviewId.equals(interviews.get(i).getId() + "")) {

                        fetchInterviewData();
                    }
                }
            }
        });
    }

    private void getFormQuestions() {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        questionsSystemViewModel.getQuestions(token, formId);
        questionsSystemViewModel.questionsMutableLiveData.observe(this, new Observer<QuestionsResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(QuestionsResults questionsResults) {
                try {
                    //binding.loadingDataConstraint.setVisibility(View.GONE);
                    questionsArrayList = questionsResults.getQuestions();
                    if (!questionsArrayList.isEmpty()) {
                        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                        getInterviewsAnswers();
                    } else
                        binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }
            }
        });
    }

    private void getFormQuestionsNoNet() {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        questionsViewModel.getAllQuestions().observe(this, new Observer<List<Questions>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(@Nullable List<Questions> questions) {
                questionsArrayList = new ArrayList<>();
                for (int i = 0; i < questions.size(); i++) {
                    Log.d("formId", "::" + formId + "::" + questions.size());
                    if (questions.get(i).getForm_fk_id().equals(formId)) {
                        Questions question = questions.get(i);
                        questionsArrayList.add(question);
                    }
                }
                if (questionsArrayList.isEmpty())
                    binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
                else {
                    binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                    getInterviewsAnswersNoNet();
                }
                // questionsAdapter.setList(questionsArrayList);
                // questionsAdapter.notifyDataSetChanged();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void deleteInterview() {
        binding.buttonDeleteInterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ViewInterviewActivity.this);
                LayoutInflater factory = LayoutInflater.from(ViewInterviewActivity.this);
                final View viewDialog = factory.inflate(R.layout.succes_create_dialog, null);
                ImageView close = viewDialog.findViewById(R.id.imageView6);
                imageView = viewDialog.findViewById(R.id.imageViewDialog);
                textView = viewDialog.findViewById(R.id.textViewMessage);
                imageView.setImageResource(R.drawable.loading);
                textView.setText(R.string.delete_interview_running);
                dialog.setView(viewDialog);
                dialog.show();
                if (isNetworkAvailable())
                    deleteInterviewAction();
                else
                    deleteInterviewActionNoNet();
            }
        });
    }

    private void deleteInterviewAction() {
        deleteInterviewSystemViewModel.deleteInterview(Integer.parseInt(interviewId));
        deleteInterviewSystemViewModel.deleteInterviewMutableLiveData.observe(this, new Observer<ResponseSuccess>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ResponseSuccess response) {
                try {
                    if (response.getSuccess() != null) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageResource(R.drawable.ic_baseline_done_all_24);
                                textView.setText(R.string.failed_success_delete);
                                Toast.makeText(getApplicationContext(), "Success Delete Interview", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }, 2000);
                    } else {
                        imageView.setImageResource(R.drawable.ic_baseline_error_outline_24);
                        textView.setText(R.string.failed_delete_interview);
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void deleteInterviewActionNoNet() {
        interviewsViewModel.getAllInterviews().observe(this, new Observer<List<Interview>>() {
            @Override
            public void onChanged(List<Interview> interviews) {
                for (int i = 0; i < interviews.size(); i++) {
                    if (interviewId.equals(interviews.get(i).getId() + "")) {
                        interviewsViewModel.delete(interviews.get(i));
                        saveInterviewDeletedInLocal(Integer.parseInt(interviewId));
                        deleteInterviewAnswers();
                    }
                }
            }
        });
    }

    private void saveInterviewDeletedInLocal(int interviewId) {
        Interview interview = new Interview();
        interview.setId(interviewId);
        interviewDeleted.add(interview);
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(interviewDeleted);
        editor.putString("task list", json);
        editor.apply();
    }

    private ArrayList<Interview> interviewDeleted;

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
    }

    private void deleteInterviewAnswers() {
        answersViewModel.getAllAnswers().observe(this, new Observer<List<Answer>>() {
            @Override
            public void onChanged(List<Answer> answers) {
                for (int i = 0; i < answers.size(); i++) {
                    if (interviewId.equals(answers.get(i).getInterview_fk_id() + "")) {
                        answersViewModel.delete(answers.get(i));
                    }
                }
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void fetchInterviewData() {
        binding.textViewErrorLocation.setVisibility(View.VISIBLE);
        binding.textViewErrorLocation.setText("Current Location: " + interviewLocation + ", " + latitude + ", " + longitude);
        binding.textViewErrorLocation.setTextColor(getColor(R.color.teal_700));
        binding.editTextInterviewTitle.setText(interviewTitle);
        // binding.editTextInterviewTitle.setFocusable(false);
    }

    private void updateInterview() {
        binding.buttonUpdateInterview.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                //checkInterviewTitle();
                interviewTitle = binding.editTextInterviewTitle.getText().toString();
                if (interviewTitle.isEmpty()) {
                    binding.textInputLayout3.setError(getString(R.string.interview_title_required));
                    binding.editTextInterviewTitle.setError(getString(R.string.interview_title_required));
                    return;
                }
                //Check location
                if (Double.toString(latitude).isEmpty() && Double.toString(longitude).isEmpty()) {
                    binding.textViewErrorLocation.setVisibility(View.VISIBLE);
                    binding.textViewErrorLocation.setText(getString(R.string.the_location_required));
                    binding.textViewErrorLocation.setTextColor(getColor(R.color.danger));
                    return;
                }
                Interview interview = new Interview(Integer.parseInt(interviewId), formId, interviewTitle, interviewLocation, latitude + "", longitude + "", PreferenceUtils.getUserId(getApplicationContext()), interviewCreatedAt, false, false);
                showDialog();
                if (isNetworkAvailable())
                    postUpdateInterview(interview);
                else {
                    interview.setUpdate_in_local(true);
                    postUpdateInterviewNoNet(interview);
                }
                //updateAnswerInterview();
            }
        });
    }

    private void postUpdateInterview(Interview interview) {
        updateInterviewSystemViewModel.updateInterview(interview);
        updateInterviewSystemViewModel.updateInterviewMutableLiveData.observe(this, new Observer<Interview>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(Interview interview) {
                try {
                    /* Success*/
                    Log.d("interview.getInterview_id()", interview.getInterview_id());
                    questionAnswersArrayList.clear();
                    answersArrayList.clear();
                    postUpdateInterviewNoNetWithoutAnswers(interview);
                    adapterQuestionAnswers(interview.getInterview_id());
                } catch (Exception e) {

                }
            }
        });
    }

    private void postUpdateInterviewNoNet(Interview interview) {
        interviewsViewModel.getAllInterviews().observe(this, new Observer<List<Interview>>() {
            @Override
            public void onChanged(List<Interview> interviews) {
                for (int i = 0; i < interviews.size(); i++) {
                    if (interviewId.equals(interviews.get(i).getId() + "")) {
                        interviewsViewModel.delete(interviews.get(i));
                        interviewsViewModel.insert(interview);
                        adapterQuestionAnswers(interviewId);
                    }
                }
            }
        });
    }

    private void postUpdateInterviewNoNetWithoutAnswers(Interview interview) {
        interviewsViewModel.getAllInterviews().observe(this, new Observer<List<Interview>>() {
            @Override
            public void onChanged(List<Interview> interviews) {
                for (int i = 0; i < interviews.size(); i++) {
                    if (interviewId.equals(interviews.get(i).getId() + "")) {
                        interviewsViewModel.delete(interviews.get(i));
                        interviewsViewModel.insert(interview);
                    }
                }
            }
        });
    }

    private void adapterQuestionAnswers(String interviewId) {
        questionAnswersArrayList = questionsAdapter.getAnswerArrayList();
        if (answersArrayList.size() != questionAnswersArrayList.size()) {
            for (int i = 0; i < questionAnswersArrayList.size(); i++) {
                Questions questions = questionAnswersArrayList.get(i);
                String questionsId = String.valueOf(questions.getId());
                Answer answer = getObjectFromString(questions.getAnswer());
                answer.setInterview_fk_id(interviewId);
                if (!isNetworkAvailable() && !answer.getType().equals("4"))
                    answer.setUpdate_in_local(true);
                /*Get image answer if not empty*/
                for (int j = 0; j < imageAnswersList.size(); j++) {
                    String question_fk_id = imageAnswersList.get(j).getQuestions_fk_id();
                    if (question_fk_id.equals(questionsId)) {
                        answer.setAnswer(imageAnswersList.get(j).getAnswer());
                        answer.setUpdate_in_local(true);
                    }
                }
                answersArrayList.add(answer);
            }
            PostAnswersList postAnswersList = new PostAnswersList(answersArrayList);
            if (isNetworkAvailable())
                postUpdateAnswer(postAnswersList);
            else
                postUpdateAnswerNoNet(answersArrayList);
        }
    }

    private ArrayList<Answer> updatedAnswerList = new ArrayList<>();

    private void postUpdateAnswerNoNet(ArrayList<Answer> answersArrayList) {
        answersViewModel.getAllAnswers().observe(this, new Observer<List<Answer>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<Answer> answers) {
                if (updatedAnswerList.size() != answersArrayList.size()) {
                    for (int i = 0; i < answersArrayList.size(); i++) {
                        Log.d("a answer okkkk", answersArrayList.get(i).getAnswer());
                        int currentId = answersArrayList.get(i).getId();
                        answers.stream()
                                .filter(answer -> currentId == answer.getId())
                                .findAny().ifPresent(oldAnswer -> answersViewModel.delete(oldAnswer));
                        answersViewModel.insert(answersArrayList.get(i));
                        updatedAnswerList.add(answersArrayList.get(i));
                    /*Answer oldAnswer = answers.stream()
                            .filter(answer -> currentId == answer.getId())
                            .findAny().orElse(null);
                    if (oldAnswer != null) {
                        answersViewModel.delete(oldAnswer);
                    } else {
                        answersViewModel.insert(answersArrayList.get(i));
                    }*/
                    }
                    imageView.setImageResource(R.drawable.success);
                    textView.setText(R.string.success_submit_interview);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(), "" + response.getSuccess(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, 2000);
                } else {
                    imageView.setImageResource(R.drawable.success);
                    textView.setText(R.string.success_submit_interview);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(), "" + response.getSuccess(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, 2000);
                }
            }
        });
    }


    private void postUpdateAnswer(PostAnswersList answer) {
        //Log.d("answer_id", "::"+answer.getAnswersList().get(0).getId());
        updateAnswerSystemViewModel.updateAnswers(answer);
        //Log.d("answersArrayList", "answersArrayList" + answer.getAnswersList().get(4).getAnswer()+"::"+ answer.getAnswersList().get(5).getAnswer());
        updateAnswerSystemViewModel.updateAnswerMutableLiveData.observe(this, new Observer<PostAnswersList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(PostAnswersList response) {
                try {
                    Log.d("onResponse", "Answer step 3 response : " + response);
                    if (response != null) {
                        /*imageView.setImageResource(R.drawable.success);
                        textView.setText(R.string.success_submit_interview);*/
                        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        postUpdateAnswerNoNet(answer.getAnswersList());
                    } else {
                        imageView.setImageResource(R.drawable.ic_baseline_error_outline_24);
                        textView.setText(R.string.failed_save_answers);
                    }

                } catch (Exception e) {
                }
            }
        });
    }

    private ArrayList<Questions> questionsRoom = new ArrayList<>();
    private ArrayList<Questions> newQuestionsForm = new ArrayList<>();

    private void getFormQuestionsRoom() {
        questionsViewModel.getAllQuestions().observe(this, new Observer<List<Questions>>() {
            @Override
            public void onChanged(List<Questions> questions) {
                if (newQuestionsForm.size() != questionsArrayList.size()) {
                    questionsRoom = new ArrayList<>();
                    newQuestionsForm = new ArrayList<>();
                    questionsRoom.addAll(questions);
                    for (int i = 0; i < questionsArrayList.size(); i++) {
                        for (int j = 0; j < questionsRoom.size(); j++) {
                            if (questionsRoom.get(j).getId() == questionsArrayList.get(i).getId()) {
                                //remove then insert again mean (update item)
                                questionsViewModel.delete(questionsRoom.get(j));
                            }
                        }
                        //insert
                        newQuestionsForm.add(questionsArrayList.get(i));
                        questionsViewModel.insert(questionsArrayList.get(i));
                    }
                }
            }
        });
    }

    private ArrayList<Questions> questionsArrayListLocal;

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkInterviewTitle() {
        interviewTitle = binding.editTextInterviewTitle.getText().toString();
        if (interviewTitle.isEmpty()) {
            binding.editTextInterviewTitle.setError(getString(R.string.interview_title_required));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showDialog() {
        AlertDialog.Builder alertadd = new AlertDialog.Builder(ViewInterviewActivity.this);
        LayoutInflater factory = LayoutInflater.from(ViewInterviewActivity.this);
        final View view = factory.inflate(R.layout.succes_create_dialog, null);
        ImageView close = view.findViewById(R.id.imageView6);
        imageView = view.findViewById(R.id.imageViewDialog);
        textView = view.findViewById(R.id.textViewMessage);
        imageView.setImageResource(R.drawable.loading);
        textView.setText(R.string.running_to_save_interview);
        alertadd.setView(view);
        alertadd.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void loadingDataDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ViewInterviewActivity.this);
        LayoutInflater factory = LayoutInflater.from(ViewInterviewActivity.this);
        final View view = factory.inflate(R.layout.alert_dialog, null);
        /*ImageView close = view.findViewById(R.id.imageView6);
        imageView = view.findViewById(R.id.imageViewDialog);
        textView = view.findViewById(R.id.textViewMessage);
        imageView.setImageResource(R.drawable.loading);
        textView.setText(R.string.running_to_save_interview);*/
        dialog.setView(view);
        dialog.show();
    }

    public Answer getObjectFromString(String jsonString) {

        Type ansType = new TypeToken<Answer>() {
        }.getType();
        Answer answer = new Gson().fromJson(jsonString, ansType);
        return answer;

    }

    private void updateAnswerInterview() {
        questionAnswersArrayList = questionsAdapter.getAnswerArrayList();
        for (int i = 0; i < questionAnswersArrayList.size(); i++) {
            Questions questions = questionAnswersArrayList.get(i);
            Answer answer = getObjectFromString(questions.getAnswer());
            answer.setInterview_fk_id(interviewId);
            for (int j = 0; j < imageAnswersList.size(); j++) {
                if (imageAnswersList.get(j).getQuestions_fk_id().equals(questions.getId()))
                    answer.setAnswer(imageAnswersList.get(j).getAnswer());
            }
            answersArrayList.add(answer);
            //postAnswer(answer);
        }
        PostAnswersList postAnswersList = new PostAnswersList(answersArrayList);
        updateAnswers(postAnswersList);
    }

    private void updateAnswers(PostAnswersList answer) {
        updateAnswerSystemViewModel.updateAnswers(answer);
        updateAnswerSystemViewModel.updateAnswerMutableLiveData.observe(this, new Observer<PostAnswersList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(PostAnswersList response) {
                try {
                    if (response != null) {
                        /*imageView.setImageResource(R.drawable.success);
                        textView.setText(R.string.success_submit_interview);*/
                        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                    } else {
                        imageView.setImageResource(R.drawable.ic_baseline_error_outline_24);
                        textView.setText(R.string.failed_save_answers);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public String stringFromObject(Answer answer) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(answer);
        return jsonString;
    }

    private boolean isRunning = false;

    private void getInterviewsAnswers() {
        isRunning = true;
        questionsSystemViewModel.getAnswers(token, String.valueOf(interviewId));
        answersSystemViewModel.answersMutableLiveData.observe(this, new Observer<AnswersResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(AnswersResults answersResults) {
                try {
                    answersFromDbArrayList = answersResults.getAnswers();
                    if (!answersFromDbArrayList.isEmpty()) {
                        for (int i = 0; i < answersFromDbArrayList.size(); i++) {
                            Answer answer = answersFromDbArrayList.get(i);
                            String question_fk_id = answer.getQuestions_fk_id();
                            for (int j = 0; j < questionsArrayList.size(); j++) {
                                String questionId = String.valueOf(questionsArrayList.get(j).getId());
                                if (questionId.equals(question_fk_id)) {
                                    questionsArrayList.get(j).setAnswer(stringFromObject(answer));
                                }
                            }
                        }
                        binding.loadingDataConstraint.setVisibility(View.GONE);
                        questionsAdapter.setAnswersFromDbList(answersFromDbArrayList);
                        questionsAdapter.setList(questionsArrayList);
                        questionsAdapter.setLocal(false);
                        questionsAdapter.notifyDataSetChanged();
                        getAnswersRoom();
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private ArrayList<Answer> answersRoom = new ArrayList<>();
    private ArrayList<Answer> newAnswersRoom = new ArrayList<>();

    private void getAnswersRoom() {
        Log.d("getAnswersRoom1", "getAnswersRoom");
        answersViewModel.getAllAnswers().observe(this, new Observer<List<Answer>>() {
            @Override
            public void onChanged(List<Answer> answers) {
                Log.d("getAnswersRoom2", "getAnswersRoom");
                Log.d("getAnswersRoomSize", answers.size() + "");
                if (newAnswersRoom.size() != answersFromDbArrayList.size()) {
                    Log.d("getAnswersRoom3", "getAnswersRoom");
                    answersRoom = new ArrayList<>();
                    newAnswersRoom = new ArrayList<>();
                    answersRoom.addAll(answers);
                    for (int i = 0; i < answersFromDbArrayList.size(); i++) {
                        for (int j = 0; j < answersRoom.size(); j++) {
                            if (answersRoom.get(j).getInterview_fk_id().equals(interviewId))
                                answersViewModel.delete(answersRoom.get(j));
                        }
                        //insert
                        newAnswersRoom.add(answersFromDbArrayList.get(i));
                        answersViewModel.insert(answersFromDbArrayList.get(i));
                        Log.d("answersViewModel.insert", "insert" + answersFromDbArrayList.get(i).getAnswer());
                    }
                    isRunning = false;
                }
            }
        });
    }


    private ArrayList<Answer> answersArrayListLocal;

    private void getInterviewsAnswersNoNet() {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        answersViewModel.getAllAnswers().observe(this, new Observer<List<Answer>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(@Nullable List<Answer> answers) {
                assert answers != null;
                Log.d("getAnswersRoomSizeNoNet", answers.size() + "");
                if (!answers.isEmpty()) {
                    binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                    answersFromDbArrayList = new ArrayList<>();
                    for (int i = 0; i < answers.size(); i++) {
                        if (answers.get(i).getInterview_fk_id().equals(interviewId))
                            answersFromDbArrayList.add(answers.get(i));
                    }
                    for (int i = 0; i < answersFromDbArrayList.size(); i++) {
                        Answer answer = answersFromDbArrayList.get(i);
                        String question_fk_id = answer.getQuestions_fk_id();
                        for (int j = 0; j < questionsArrayList.size(); j++) {
                            String questionId = String.valueOf(questionsArrayList.get(j).getId());
                            if (questionId.equals(question_fk_id)) {
                                questionsArrayList.get(j).setAnswer(stringFromObject(answer));
                                //Log.d("postionQuestion","answer.getAnswer()"+answer.getAnswer());
                                //holder.editTextQuestion.setText(answer.getAnswer().toString());
                            }
                        }
                    }
                    binding.loadingDataConstraint.setVisibility(View.GONE);
                    answersArrayListLocal = new ArrayList<>();
                    answersArrayListLocal.addAll(answersFromDbArrayList);
                    questionsAdapter.setAnswersFromDbList(answersFromDbArrayList);
                    questionsAdapter.setList(questionsArrayList);
                    questionsAdapter.setLocal(true);
                    questionsAdapter.notifyDataSetChanged();
                    /*questionsAdapter.setAnswersFromDbList(answersFromDbArrayList);
                    questionsAdapter.notifyDataSetChanged();*/
                } else
                    binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
            }
        });
    }


    private void getLocation() {
        binding.constraintLayoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable())
                    getCurrentLocation();
                else
                    getCurrentLocationNoNet();
            }
        });
    }

    /*Map section*/
    @SuppressLint({"MissingPermission", "SetTextI18n"})
    private void getCurrentLocationNoNet() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new MyLocationListener();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Geocoder geocoder;
                ArrayList<Address> addresses = new ArrayList<>();
                geocoder = new Geocoder(ViewInterviewActivity.this, Locale.getDefault());
                try {
                    addresses = (ArrayList<Address>) geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() > 0) {
                    interviewLocation = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                }
                binding.textViewErrorLocation.setVisibility(View.VISIBLE);
                binding.textViewErrorLocation.setTextColor(getColor(R.color.teal_700));
                binding.textViewErrorLocation.setText("Current Location: " + interviewLocation + ", " + latitude + ", " + longitude);
                showLocationInMap();
            } else {
                binding.textViewErrorLocation.setVisibility(View.VISIBLE);
                binding.textViewErrorLocation.setTextColor(getColor(R.color.danger));
                binding.textViewErrorLocation.setText("No Location!");
            }
        }
    }

    /*Map section*/
    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(ViewInterviewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (isGPSEnabled()) {

                LocationServices.getFusedLocationProviderClient(ViewInterviewActivity.this)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                LocationServices.getFusedLocationProviderClient(ViewInterviewActivity.this)
                                        .removeLocationUpdates(this);

                                if (locationResult != null && locationResult.getLocations().size() > 0) {

                                    int index = locationResult.getLocations().size() - 1;
                                    latitude = locationResult.getLocations().get(index).getLatitude();
                                    longitude = locationResult.getLocations().get(index).getLongitude();
                                    Geocoder geocoder;
                                    ArrayList<Address> addresses = new ArrayList<>();
                                    geocoder = new Geocoder(ViewInterviewActivity.this, Locale.getDefault());
                                    try {
                                        addresses = (ArrayList<Address>) geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (addresses.size() > 0) {
                                        interviewLocation = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                    }
                                    String city = addresses.get(0).getLocality();
                                    String state = addresses.get(0).getAdminArea();
                                    String country = addresses.get(0).getCountryName();
                                    String postalCode = addresses.get(0).getPostalCode();
                                    String knownName = addresses.get(0).getFeatureName();

                                    binding.textViewErrorLocation.setVisibility(View.VISIBLE);
                                    binding.textViewErrorLocation.setText("Current Location: " + interviewLocation + ", " + latitude + ", " + longitude);
                                    binding.textViewErrorLocation.setTextColor(getColor(R.color.teal_700));

                                    showLocationInMap();
                                }
                            }
                        }, Looper.getMainLooper());

            } else {
                turnOnGPS();
            }

        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }

    private AlertDialog alertDialog;

    @SuppressLint("SetTextI18n")
    private void showLocationInMap() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        //AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewInterviewActivity.this);
        LayoutInflater factory = LayoutInflater.from(ViewInterviewActivity.this);
        if (viewMap != null) {
            ViewGroup parent = (ViewGroup) viewMap.getParent();
            if (parent != null)
                parent.removeView(viewMap);
        }
        try {
            viewMap = factory.inflate(R.layout.map_dialog, null);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        dialogBuilder.setView(viewMap);
        TextView textView = viewMap.findViewById(R.id.textViewLocation);
        textView.setText(interviewLocation + ", " + latitude + ", " + longitude);
        ImageView imageViewClose = viewMap.findViewById(R.id.imageView6);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(ViewInterviewActivity.this);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        //alertDialog.setView(viewMap);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(ViewInterviewActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(ViewInterviewActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    getCurrentLocation();

                } else {

                    turnOnGPS();
                }
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap = googleMap;
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng).title(interviewLocation));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));

    }

    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            loc.getLatitude();
            loc.getLongitude();
            String Text = "My current location is: " + "Latitude = "
                    + loc.getLatitude() + "Longitude = " + loc.getLongitude();
            //Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_SHORT).show();
            //Log.d("TAG", "Starting..");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
    /*End Map */

    public static final String IMAGE_NAME ="IMAGE_NAME" ;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                postImage();
                //Toast.makeText(getApplicationContext(), imageUri + "", Toast.LENGTH_SHORT).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public int position;
    public void setPosition(int position) {
        this.position = position;
    }

    private void postImage() {
        if (imageUri != null) {
            compressAndNameImage(imageUri);
            ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
            compressor.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayInputStream);
            byte[] thumpData = byteArrayInputStream.toByteArray();
            String imageAnswer = Base64.encodeToString(thumpData, Base64.DEFAULT);
            Answer answer = new Answer(questionId, "", imageAnswer, "4");
            imageAnswersList.add(answer);
            Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
            questionsArrayList.get(position).setAnswer(stringFromObject(answer));
            /*for (int i = 0; i < questionsArrayList.size(); i++) {
                if (questionsArrayList.get(i).getId() == Integer.parseInt(questionId)) {
                    questionsArrayList.get(i).setAnswer(stringFromObject(answer));
                    //questionsAdapter.notifyDataSetChanged();
                }
            }*/
        }
    }

    private void compressAndNameImage(Uri imageUri) {
        imageName = random() + ".jpg";
        File imageFile = new File(imageUri.getPath());
        try {
            compressor = new Compressor(ViewInterviewActivity.this)
                    .setMaxHeight(240)
                    .setMaxWidth(360)
                    .setQuality(5)
                    .compressToBitmap(imageFile);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    //Name image
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

   /* @Override
    public void onBackPressed() {
        if (isNetworkAvailable()) {
            if (!isRunning) {
                Toast.makeText(getApplicationContext(), "Please Wait to Sync Data...", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Done.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, 3000);
            } else
                Toast.makeText(getApplicationContext(), "Please Wait to Sync Data...", Toast.LENGTH_SHORT).show();
        }else
            super.onBackPressed();
    }*/
}