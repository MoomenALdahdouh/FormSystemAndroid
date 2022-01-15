package com.example.formsystem.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.formsystem.R;
import com.example.formsystem.adapter.QuestionsAdapter;
import com.example.formsystem.databinding.ActivityMakeInterviewBinding;
import com.example.formsystem.model.Answer;
import com.example.formsystem.model.Interview;
import com.example.formsystem.model.PostAnswersList;
import com.example.formsystem.model.Questions;
import com.example.formsystem.model.QuestionsResults;
import com.example.formsystem.utils.PreferenceUtils;
import com.example.formsystem.viewmodel.FormSystemViewModel;
import com.example.formsystem.viewmodel.local.FormViewModel;
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
import com.theartofdev.edmodo.cropper.CropImageView;

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

public class MakeInterviewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityMakeInterviewBinding binding;
    private String formId;
    private FormSystemViewModel questionsSystemViewModel;
    private FormSystemViewModel postInterviewSystemViewModel;
    private FormSystemViewModel postAnswerSystemViewModel;
    private QuestionsViewModel questionsViewModel;
    private InterviewsViewModel interviewsViewModel;
    private String token;
    private String userId;
    private RecyclerView recyclerView;
    private QuestionsAdapter questionsAdapter;
    private ArrayList<Questions> questionsArrayList;
    private ArrayList<Questions> questionAnswersArrayList;
    private ArrayList<Answer> answersArrayList;
    private ImageView imageView;
    private TextView textView;
    private String interviewTitle = "";
    private String interviewLocation = "";
    private double latitude;
    private double longitude;
    private LocationRequest locationRequest;
    private GoogleMap googleMap;
    private View viewMap;
    private LocationManager locationManager;
    private String provider;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeInterviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        token = PreferenceUtils.getToken(MakeInterviewActivity.this);
        userId = PreferenceUtils.getToken(MakeInterviewActivity.this);
        questionsSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        postInterviewSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        postAnswerSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        questionsViewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
        interviewsViewModel = new ViewModelProvider(this).get(InterviewsViewModel.class);
        recyclerView = binding.recyclerView;
        questionAnswersArrayList = new ArrayList<>();
        answersArrayList = new ArrayList<>();
        questionsAdapter = new QuestionsAdapter(MakeInterviewActivity.this);
        questionsArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        questionsAdapter.setList(questionsArrayList);
        questionsAdapter.isUpdateInterview(false);
        recyclerView.setAdapter(questionsAdapter);
        recyclerView.setHasFixedSize(true);
        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
        binding.loadingDataConstraint.setVisibility(View.GONE);
        binding.textViewErrorLocation.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ViewActivitiesActivity.FORM_ID)) {
            formId = intent.getStringExtra(ViewActivitiesActivity.FORM_ID);
            if (isNetworkAvailable()) {
                getFormQuestions();
                //submitInterview();
                //getCurrentLocation();
            } else {
                getFormQuestionsNoNet();
            }
            submitInterview();
            getLocation();
        }
        /*binding.buttonSubmitInterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int time1 = (int) System.currentTimeMillis();
                Toast.makeText(getApplicationContext(), time1,Toast.LENGTH_SHORT).show();
            }
        });*/
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

    private void submitInterview() {
        binding.buttonSubmitInterview.setOnClickListener(new View.OnClickListener() {
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
                //interviewLocation.isEmpty() ||
                if ((Double.toString(latitude).isEmpty() && Double.toString(longitude).isEmpty())) {
                    binding.textViewErrorLocation.setVisibility(View.VISIBLE);
                    binding.textViewErrorLocation.setText(getString(R.string.the_location_required));
                    binding.textViewErrorLocation.setTextColor(getColor(R.color.danger));
                    return;
                }
                int id = (int) System.currentTimeMillis();
                Interview interview = new Interview(id, formId, interviewTitle, interviewLocation, latitude + "", longitude + "", PreferenceUtils.getUserId(getApplicationContext()));
                showDialog();
                if (isNetworkAvailable())
                    postInterview(interview);
                else {
                    Toast.makeText(getApplicationContext(), "getInterviewTitle::" + interviewTitle, Toast.LENGTH_LONG).show();
                    postInterviewNoNet(interview);
                }
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
        AlertDialog.Builder alertadd = new AlertDialog.Builder(MakeInterviewActivity.this);
        LayoutInflater factory = LayoutInflater.from(MakeInterviewActivity.this);
        final View view = factory.inflate(R.layout.succes_create_dialog, null);
        ImageView close = view.findViewById(R.id.imageView6);
        imageView = view.findViewById(R.id.imageViewDialog);
        textView = view.findViewById(R.id.textViewMessage);
        imageView.setImageResource(R.drawable.loading);
        textView.setText(R.string.running_to_submit_interview);
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
                    Log.d("interview.getInterview_id()", interview.getInterview_id());
                    questionAnswersArrayList.clear();
                    answersArrayList.clear();
                    adapterQuestionAnswers(interview.getInterview_id());
                } catch (Exception e) {
                }
            }
        });
    }

    private void postInterviewNoNet(Interview interview) {
        interviewsViewModel.insert(interview);
        Toast.makeText(getApplicationContext(), "getInterview_id::" + interview.getId(), Toast.LENGTH_LONG).show();
        //Log.d("getInterview_id::", interview.getInterview_id());
        adapterQuestionAnswers(interview.getId() + "");
    }

    public Answer getObjectFromString(String jsonString) {

        Type ansType = new TypeToken<Answer>() {
        }.getType();
        Answer answer = new Gson().fromJson(jsonString, ansType);
        return answer;

    }

    private void adapterQuestionAnswers(String interviewId) {
        questionAnswersArrayList = questionsAdapter.getAnswerArrayList();
        for (int i = 0; i < questionAnswersArrayList.size(); i++) {
            Questions questions = questionAnswersArrayList.get(i);
            String questionsId = String.valueOf(questions.getId());
            Answer answer = getObjectFromString(questions.getAnswer());
            //Log.d("answer_id", answer.getId() + "::" + answer.getAnswer() + "::" + answer.getInterview_fk_id());
            answer.setInterview_fk_id(interviewId);
            /*Get image answer if not empty*/
            for (int j = 0; j < imageAnswersList.size(); j++) {
                String question_fk_id = imageAnswersList.get(j).getQuestions_fk_id();
                //Log.d("answersArrayList", "size"+imageAnswersList.size()+"answersArrayList" + imageAnswersList.get(j).getAnswer());
                if (question_fk_id.equals(questionsId)) {
                    answer.setAnswer(imageAnswersList.get(j).getAnswer());
                    //Log.d("answersArrayList", "answersArrayList" + imageAnswersList.get(j).getAnswer());
                }
            }
            answersArrayList.add(answer);
            //Log.d("onResponse", "answersArrayList" + answersArrayList.get(i).getAnswer());
            //postAnswer(answer);
        }
        PostAnswersList postAnswersList = new PostAnswersList(answersArrayList);
        postAnswer(postAnswersList);
    }

    private void postAnswer(PostAnswersList answer) {
        //Log.d("answer_id", "::"+answer.getAnswersList().get(0).getId());
        postAnswerSystemViewModel.postAnswer(answer);
        //Log.d("answersArrayList", "answersArrayList" + answer.getAnswersList().get(4).getAnswer()+"::"+ answer.getAnswersList().get(5).getAnswer());
        postAnswerSystemViewModel.postAnswerMutableLiveData.observe(this, new Observer<PostAnswersList>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(PostAnswersList response) {
                try {
                    Log.d("onResponse", "Answer step 3 response : " + response);
                    if (response != null) {
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


    private void getFormQuestions() {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        questionsSystemViewModel.getQuestions(token, formId);
        questionsSystemViewModel.questionsMutableLiveData.observe(this, new Observer<QuestionsResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(QuestionsResults questionsResults) {
                try {
                    binding.loadingDataConstraint.setVisibility(View.GONE);
                    questionsArrayList = questionsResults.getQuestions();
                    if (!questionsArrayList.isEmpty()) {
                        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                        questionsAdapter.setList(questionsArrayList);
                        questionsAdapter.notifyDataSetChanged();
                        //Replace old data in room
                        if (newQuestionsForm.size() != questionsArrayList.size())
                            getFormQuestionsRoom();
                    } else
                        binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
                } catch (Exception e) {

                }
            }
        });
    }

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

    private void getFormQuestionsNoNet() {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        questionsViewModel.getAllQuestions().observe(this, new Observer<List<Questions>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(@Nullable List<Questions> questions) {
                binding.loadingDataConstraint.setVisibility(View.GONE);
                assert questions != null;
                if (!questions.isEmpty()) {
                    binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                    questionsArrayList = new ArrayList<>();
                    for (int i = 0; i < questions.size(); i++) {
                        if (questions.get(i).getForm_fk_id().equals(formId))
                            questionsArrayList.add(questions.get(i));
                    }
                    questionsArrayListLocal = new ArrayList<>();
                    questionsArrayListLocal.addAll(questionsArrayList);
                    questionsAdapter.setList(questionsArrayList);
                    questionsAdapter.notifyDataSetChanged();
                } else
                    binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
            }
        });
    }


    /*Map section*/
    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(MakeInterviewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (isGPSEnabled()) {

                LocationServices.getFusedLocationProviderClient(MakeInterviewActivity.this)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @SuppressLint({"SetTextI18n", "ResourceAsColor"})
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                LocationServices.getFusedLocationProviderClient(MakeInterviewActivity.this)
                                        .removeLocationUpdates(this);

                                if (locationResult != null && locationResult.getLocations().size() > 0) {

                                    int index = locationResult.getLocations().size() - 1;
                                    latitude = locationResult.getLocations().get(index).getLatitude();
                                    longitude = locationResult.getLocations().get(index).getLongitude();
                                    Geocoder geocoder;
                                    ArrayList<Address> addresses = new ArrayList<>();
                                    geocoder = new Geocoder(MakeInterviewActivity.this, Locale.getDefault());
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
        //AlertDialog.Builder alertDialog = new AlertDialog.Builder(MakeInterviewActivity.this);
        LayoutInflater factory = LayoutInflater.from(MakeInterviewActivity.this);
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
        mapFragment.getMapAsync(MakeInterviewActivity.this);
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
                    Toast.makeText(MakeInterviewActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MakeInterviewActivity.this, 2);
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

    private String imageName;
    private Bitmap compressor;
    private String downloadUri = null;
    private Uri imageUri = null;
    private static final int MAX_LENGTH = 100;
    private String imageUrl = "";

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

    public String questionId;
    private ArrayList<Answer> imageAnswersList = new ArrayList<>();

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
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
            Log.d("onResponse", "Answer step 1: " + answer.getAnswer());
            Toast.makeText(getApplicationContext(), imageName, Toast.LENGTH_LONG).show();
            //StorageReference filePath = storageReference.child("category_image/").child(imageName);
            // UploadTask uploadTask = filePath.putBytes(thumpData);
            /*Post*/
        }
    }

    private void compressAndNameImage(Uri imageUri) {
        imageName = random() + ".jpg";
        File imageFile = new File(imageUri.getPath());
        try {
            compressor = new Compressor(MakeInterviewActivity.this)
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

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
                geocoder = new Geocoder(MakeInterviewActivity.this, Locale.getDefault());
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
    /* End of Class MyLocationListener */
    /* End of UseGps Activity */

}