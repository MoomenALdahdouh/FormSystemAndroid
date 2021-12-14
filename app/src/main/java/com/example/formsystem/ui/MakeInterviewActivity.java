package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.formsystem.adapter.ActivitiesAdapter;
import com.example.formsystem.adapter.InterviewsAdapter;
import com.example.formsystem.adapter.QuestionsAdapter;
import com.example.formsystem.databinding.ActivityMainBinding;
import com.example.formsystem.databinding.ActivityMakeInterviewBinding;
import com.example.formsystem.model.Answer;
import com.example.formsystem.model.Interview;
import com.example.formsystem.model.InterviewResults;
import com.example.formsystem.model.PostAnswersList;
import com.example.formsystem.model.Questions;
import com.example.formsystem.model.QuestionsResults;
import com.example.formsystem.utils.PreferenceUtils;
import com.example.formsystem.viewmodel.FormSystemViewModel;

import java.util.ArrayList;

public class MakeInterviewActivity extends AppCompatActivity {

    private ActivityMakeInterviewBinding binding;
    private String formId;
    private FormSystemViewModel questionsSystemViewModel;
    private FormSystemViewModel postInterviewSystemViewModel;
    private FormSystemViewModel postAnswerSystemViewModel;
    private String token;
    private String userId;
    private RecyclerView recyclerView;
    private QuestionsAdapter questionsAdapter;
    private ArrayList<Questions> questionsArrayList;
    private ArrayList<Questions> questionAnswersArrayList;
    private ArrayList<Answer> answersArrayList;

    public MakeInterviewActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeInterviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        token = PreferenceUtils.getToken(MakeInterviewActivity.this);
        userId = PreferenceUtils.getToken(MakeInterviewActivity.this);
        questionsSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        postInterviewSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        postAnswerSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        recyclerView = binding.recyclerView;
        questionAnswersArrayList = new ArrayList<>();
        answersArrayList = new ArrayList<>();
        questionsAdapter = new QuestionsAdapter(MakeInterviewActivity.this);
        questionsArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        questionsAdapter.setList(questionsArrayList);
        recyclerView.setAdapter(questionsAdapter);
        recyclerView.setHasFixedSize(true);
        binding.constraintLayoutEmptyData.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ViewActivitiesActivity.FORM_ID)) {
            formId = intent.getStringExtra(ViewActivitiesActivity.FORM_ID);
            //binding.textView9.setText(formId);
            getFormQuestions();
            submitInterview();
        }
    }

    private void submitInterview() {
        binding.buttonSubmitInterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Interview interview = new Interview(formId, "new interview", "Palestine");
                postInterview(interview);
            }
        });
    }

    private void postInterview(Interview interview) {
        postInterviewSystemViewModel.postInterview(interview);
        postInterviewSystemViewModel.postInterviewMutableLiveData.observe(this, new Observer<Interview>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(Interview interview) {
                try {
                    /* Success*/
                    String sd = interview.getInterview_id();
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
            //answersArrayList.add(answer);
            postAnswer(answer);
        }
        //PostAnswersList postAnswersList = new PostAnswersList(answersArrayList);
        //postAnswer(postAnswersList);
    }

    private void postAnswer(Answer answer) {
        postAnswerSystemViewModel.postAnswer(answer);
        postAnswerSystemViewModel.postAnswerMutableLiveData.observe(this, new Observer<Answer>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(Answer response) {
                try {
                    Toast.makeText(getApplicationContext(), "" + response, Toast.LENGTH_SHORT).show();
                    /*Success*/
                    // finish();
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
                    } else
                        binding.constraintLayoutEmptyData.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }
            }
        });
    }
}