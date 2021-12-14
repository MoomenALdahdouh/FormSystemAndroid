package com.example.formsystem.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formsystem.R;
import com.example.formsystem.model.Interview;
import com.example.formsystem.model.Questions;
import com.example.formsystem.ui.ViewInterviewActivity;

import java.util.ArrayList;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    public static final String QUESTION_ID = "QUESTION_ID";
    private Context context;
    private ArrayList<Questions> questionsArrayList;

    public QuestionsAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<Questions> interviewsArrayList) {
        this.questionsArrayList = interviewsArrayList;
    }

    @NonNull
    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.question_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.ViewHolder holder, int position) {
        try {
            Questions questions = questionsArrayList.get(position);
            holder.textViewQuestionTitle.setText(questions.getTitle());
        } catch (Exception exception) {

        }
    }

    @Override
    public int getItemCount() {
        return questionsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestionTitle;
        EditText editTextQuestion;
        ConstraintLayout constraintLayoutInterview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestionTitle = itemView.findViewById(R.id.textViewQuestionTitle);
            editTextQuestion = itemView.findViewById(R.id.editTextQuestion);
            //constraintLayoutInterview = itemView.findViewById(R.id.interviewItemId);
            /*constraintLayoutInterview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = getAdapterPosition();
                    Questions questionClicked = questionsArrayList.get(adapterPosition);
                    String interviewId = questionClicked.getId();
                    Intent intent = new Intent(context, ViewInterviewActivity.class);
                    intent.putExtra(QUESTION_ID, interviewId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });*/
        }
    }
}