package com.example.formsystem.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.formsystem.databinding.ActivityMakeInterviewBinding;
import com.example.formsystem.databinding.ActivityViewInterviewBinding;
import com.example.formsystem.model.Answer;
import com.example.formsystem.model.AnswersResults;
import com.example.formsystem.model.Interview;
import com.example.formsystem.model.PostAnswersList;
import com.example.formsystem.model.Questions;
import com.example.formsystem.model.QuestionsResults;
import com.example.formsystem.utils.PreferenceUtils;
import com.example.formsystem.viewmodel.FormSystemViewModel;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class ViewInterviewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityViewInterviewBinding binding;
    private FormSystemViewModel questionsSystemViewModel;
    private FormSystemViewModel answersSystemViewModel;
    private FormSystemViewModel postInterviewSystemViewModel;
    private FormSystemViewModel postAnswerSystemViewModel;
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
    private String formId;
    private String interviewId = "";
    private double latitude;
    private double longitude;
    private LocationRequest locationRequest;
    private GoogleMap googleMap;
    private View viewMap;

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
        postAnswerSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        recyclerView = binding.recyclerView;
        questionAnswersArrayList = new ArrayList<>();
        answersArrayList = new ArrayList<>();
        answersFromDbArrayList = new ArrayList<>();
        questionsAdapter = new QuestionsAdapter(ViewInterviewActivity.this);
        questionsArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        questionsAdapter.setList(questionsArrayList);
        questionsAdapter.isUpdateInterview(true);
        recyclerView.setAdapter(questionsAdapter);
        recyclerView.setHasFixedSize(true);
        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
        binding.textViewErrorLocation.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(InterviewsAdapter.FORM_ID)) {
            formId = intent.getStringExtra(InterviewsAdapter.FORM_ID);
            interviewId = intent.getStringExtra(InterviewsAdapter.INTERVIEW_ID);
            interviewTitle = intent.getStringExtra(InterviewsAdapter.INTERVIEW_TITLE);
            interviewLocation = intent.getStringExtra(InterviewsAdapter.INTERVIEW_LOCATION);
            latitude = Double.parseDouble(intent.getStringExtra(InterviewsAdapter.INTERVIEW_LATITUDE));
            longitude = Double.parseDouble(intent.getStringExtra(InterviewsAdapter.INTERVIEW_LONGITUDE));
            getFormQuestions();
            updateInterview();
            getCurrentLocation();
            fetchInterviewData();
        }
    }

    @SuppressLint("SetTextI18n")
    private void fetchInterviewData() {
        binding.textViewErrorLocation.setVisibility(View.VISIBLE);
        binding.textViewErrorLocation.setText("Current Location: " + interviewLocation + ", " + latitude + ", " + longitude);
        binding.textViewErrorLocation.setTextColor(getColor(R.color.teal_700));
        binding.editTextInterviewTitle.setText(interviewTitle);

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
                if (interviewLocation.isEmpty() || (Double.toString(latitude).isEmpty() && Double.toString(longitude).isEmpty())) {
                    binding.textViewErrorLocation.setVisibility(View.VISIBLE);
                    binding.textViewErrorLocation.setText(getString(R.string.the_location_required));
                    binding.textViewErrorLocation.setTextColor(getColor(R.color.danger));
                    return;
                }
                Interview interview = new Interview(formId, interviewTitle, interviewLocation, latitude + "", longitude + "");
                showDialog();
                postInterview(interview);
            }
        });
    }

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

    private void postInterview(Interview interview) {
        postInterviewSystemViewModel.postInterview(interview);
        postInterviewSystemViewModel.postInterviewMutableLiveData.observe(this, new Observer<Interview>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(Interview interview) {
                try {
                    /* Success*/
                    questionAnswersArrayList.clear();
                    answersArrayList.clear();
                    adapterQuestionAnswers(interview.getInterview_id());
                } catch (Exception e) {
                }
            }
        });
    }

    private void adapterQuestionAnswers(String interviewId) {
        questionAnswersArrayList = questionsAdapter.getAnswerArrayList();
        for (int i = 0; i < questionAnswersArrayList.size(); i++) {
            Questions questions = questionAnswersArrayList.get(i);
            Answer answer = questions.getAnswer();
            answer.setInterview_fk_id(interviewId);
            answersArrayList.add(answer);
            //postAnswer(answer);
        }
        PostAnswersList postAnswersList = new PostAnswersList(answersArrayList);
        postAnswer(postAnswersList);
    }

    private void postAnswer(PostAnswersList answer) {
        postAnswerSystemViewModel.postAnswer(answer);
        postAnswerSystemViewModel.postAnswerMutableLiveData.observe(this, new Observer<PostAnswersList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(PostAnswersList response) {
                try {
                    imageView.setImageResource(R.drawable.success);
                    textView.setText(R.string.success_submit_interview);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(getApplicationContext(), "" + response.getSuccess(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, 2000);
                } catch (Exception e) {
                }
            }
        });
    }

    private void getFormQuestions() {
        questionsSystemViewModel.getQuestions(token, formId);
        questionsSystemViewModel.questionsMutableLiveData.observe(this, new Observer<QuestionsResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(QuestionsResults questionsResults) {
                try {
                    questionsArrayList = questionsResults.getQuestions();
                    if (!questionsArrayList.isEmpty()) {
                        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                        questionsAdapter.setList(questionsArrayList);
                        questionsAdapter.notifyDataSetChanged();
                        getInterviewsAnswers();
                    } else
                        binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }
            }
        });
    }

    private void getInterviewsAnswers() {
        questionsSystemViewModel.getAnswers(token, interviewId);
        answersSystemViewModel.answersMutableLiveData.observe(this, new Observer<AnswersResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(AnswersResults answersResults) {
                try {
                    answersFromDbArrayList = answersResults.getAnswers();
                    questionsAdapter.setAnswersFromDbList(answersFromDbArrayList);
                    questionsAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                }
            }
        });
    }

    /*Map section*/
    private void getCurrentLocation() {
        binding.constraintLayoutLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                            interviewLocation = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                            String city = addresses.get(0).getLocality();
                                            String state = addresses.get(0).getAdminArea();
                                            String country = addresses.get(0).getCountryName();
                                            String postalCode = addresses.get(0).getPostalCode();
                                            String knownName = addresses.get(0).getFeatureName();

                                            binding.textViewErrorLocation.setVisibility(View.VISIBLE);
                                            binding.textViewErrorLocation.setText("Current Location: " + interviewLocation + ", " + latitude + ", " + longitude);
                                            binding.textViewErrorLocation.setTextColor(getColor(R.color.teal_700));

                                            /*Map dialog*/
                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ViewInterviewActivity.this);
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
                                            //viewMap = factory.inflate(R.layout.map_dialog, null);
                                            TextView textView = viewMap.findViewById(R.id.textViewLocation);
                                            textView.setText(interviewLocation + ", " + latitude + ", " + longitude);
                                            //MapView mapView = view.findViewById(R.id.mapView);
                                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                                    .findFragmentById(R.id.mapView);
                                            mapFragment.getMapAsync(ViewInterviewActivity.this);
                                            alertDialog.setView(viewMap);
                                            alertDialog.show();
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
        });
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

    }
}