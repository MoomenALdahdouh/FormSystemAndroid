package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.formsystem.R;
import com.example.formsystem.adapter.ActivitiesAdapter;
import com.example.formsystem.adapter.FormsAdapter;
import com.example.formsystem.adapter.InterviewsAdapter;
import com.example.formsystem.databinding.ActivityViewActivitiesBinding;
import com.example.formsystem.model.Activity;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.FormResults;
import com.example.formsystem.model.Interview;
import com.example.formsystem.model.InterviewResults;
import com.example.formsystem.model.User;
import com.example.formsystem.model.UserResults;
import com.example.formsystem.utils.PreferenceUtils;
import com.example.formsystem.viewmodel.FormSystemViewModel;
import com.example.formsystem.viewmodel.local.ActivitiesViewModel;
import com.example.formsystem.viewmodel.local.FormViewModel;
import com.example.formsystem.viewmodel.local.InterviewsViewModel;
import com.example.formsystem.viewmodel.local.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;

public class ViewActivitiesActivity extends AppCompatActivity {

    public static final String FORM_ID = "FORM_ID";
    private ActivityViewActivitiesBinding binding;
    private String activityId;
    private String formId;
    private FormSystemViewModel formSystemViewModel;
    private FormSystemViewModel interviewsSystemViewModel;
    private FormViewModel formViewModel;
    private InterviewsViewModel interviewsViewModel;
    private String token;
    private String userId;
    private RecyclerView recyclerView;
    private InterviewsAdapter interviewsAdapter;
    private ArrayList<Interview> interviewsArrayList;
    private ArrayList<Interview> interviewArrayList;
    private ArrayList<Interview> interviewArrayListLocal;
    private Form form;
    private String worker_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewActivitiesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        token = PreferenceUtils.getToken(ViewActivitiesActivity.this);
        worker_id = PreferenceUtils.getUserId(getApplicationContext());
        formSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        interviewsSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        interviewsViewModel = new ViewModelProvider(this).get(InterviewsViewModel.class);
        formViewModel = new ViewModelProvider(this).get(FormViewModel.class);
        userId = PreferenceUtils.getUserId(ViewActivitiesActivity.this);
        interviewArrayList = new ArrayList<>();
        recyclerView = binding.recyclerView;
        interviewsAdapter = new InterviewsAdapter(ViewActivitiesActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        interviewsAdapter.setList(interviewArrayList);
        recyclerView.setAdapter(interviewsAdapter);
        recyclerView.setHasFixedSize(true);
        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
        binding.loadingDataConstraint.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(FormsAdapter.FORM_ID)) {
            //activityId = intent.getStringExtra(FormsAdapter.FORM_ID);
            formId = intent.getStringExtra(FormsAdapter.FORM_ID);
            makeInterviewClick();
            if (isNetworkAvailable()) {
                //getInterviewsRoom();
                getInterviews(formId, worker_id);
            } else {
                getInterviewsNoNet(formId);
            }
        }
    }

    private void getFormNet() {
        formSystemViewModel.getForm(token, activityId);
        formSystemViewModel.formMutableLiveData.observe(this, new Observer<FormResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(FormResults formResults) {
                try {
                    form = formResults.getForm();
                    formViewModel.delete(form);
                    //Save new form in local
                    formViewModel.insert(form);
                    formId = String.valueOf(form.getId());

                    getInterviews(formId, worker_id);
                    interviewsAdapter.setForm(form);
                } catch (Exception e) {

                }

            }
        });
    }

    private void getFormNoNet() {
        formViewModel.getAllForms().observe(this, new Observer<List<Form>>() {
            @Override
            public void onChanged(@Nullable List<Form> forms) {
                for (int i = 0; i < forms.size(); i++) {
                    if (forms.get(i).getActivity_fk_id().equals(activityId)) {
                        form = forms.get(i);
                        formId = String.valueOf(form.getId());
                        getInterviewsNoNet(formId);
                        interviewsAdapter.setForm(form);
                    }
                }
            }
        });
    }


    private ArrayList<Interview> interviewsRoom = new ArrayList<>();
    private ArrayList<Interview> newInterviewsRoom = new ArrayList<>();

    private void getInterviewsRoom() {
        interviewsViewModel.getAllInterviews().observe(this, new Observer<List<Interview>>() {
            @Override
            public void onChanged(List<Interview> interviews) {
                if (newInterviewsRoom.size() != interviewArrayList.size()) {
                    interviewsRoom = new ArrayList<>();
                    newInterviewsRoom = new ArrayList<>();
                    interviewsRoom.addAll(interviews);
                    for (int i = 0; i < interviewArrayList.size(); i++) {
                        for (int j = 0; j < interviewsRoom.size(); j++) {
                            if (interviewsRoom.get(j).getId() == interviewArrayList.get(i).getId()) {
                                //remove then insert again mean (update item)
                                interviewsViewModel.delete(interviewsRoom.get(j));
                            }
                        }
                        //insert
                        newInterviewsRoom.add(interviewArrayList.get(i));
                        interviewsViewModel.insert(interviewArrayList.get(i));
                    }
                    /*for (int i = 0; i < newInterviewsRoom.size(); i++) {
                        //Log.d("Item itemmmm ", "" + newInterviewsRoom.get(i).getId());
                        interviewsViewModel.insert(newInterviewsRoom.get(i));
                    }*/
                }
            }
        });
    }

    private void getInterviews(String formId, String worker_id) {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        interviewsSystemViewModel.getInterviews(token, formId, worker_id);
        interviewsSystemViewModel.interviewsMutableLiveData.observe(this, new Observer<InterviewResults>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(InterviewResults interviewResults) {
                try {
                    binding.loadingDataConstraint.setVisibility(View.GONE);
                    interviewArrayList = interviewResults.getInterviews();
                    //Replace old data in room
                    if (!interviewArrayList.isEmpty()) {
                        //Fill adapter with interviews from api
                        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                        interviewsAdapter.setList(interviewArrayList);
                        interviewsAdapter.notifyDataSetChanged();
                        interviewsAdapter.setFormId(formId);

                        if (newInterviewsRoom.size() != interviewArrayList.size())
                            getInterviewsRoom();

                    } else
                        binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }
            }
        });
    }

    private void getInterviewsNoNet(String formId) {
        binding.loadingDataConstraint.setVisibility(View.VISIBLE);
        interviewsViewModel.getAllInterviews().observe(this, new Observer<List<Interview>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(@Nullable List<Interview> interviews) {
                binding.loadingDataConstraint.setVisibility(View.GONE);
                assert interviews != null;
                if (!interviews.isEmpty()) {
                    binding.constraintLayoutEmptyData.setVisibility(View.GONE);
                    interviewArrayList = new ArrayList<>();
                    for (int i = 0; i < interviews.size(); i++) {
                        if (interviews.get(i).getForm_fk_id().equals(formId))
                            interviewArrayList.add(interviews.get(i));
                    }
                    interviewArrayListLocal = new ArrayList<>();
                    interviewArrayListLocal.addAll(interviewArrayList);
                    interviewsAdapter.setList(interviewArrayList);
                    interviewsAdapter.notifyDataSetChanged();
                    interviewsAdapter.setForm(form);
                } else
                    binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
            }
        });
    }

    private void makeInterviewClick() {
        binding.buttonMakeInterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewActivitiesActivity.this, MakeInterviewActivity.class);
                intent.putExtra(FORM_ID, formId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkAvailable()) {
            //getInterviewsRoom();
            getInterviews(formId,worker_id);
        } else {
            getInterviewsNoNet(formId);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}