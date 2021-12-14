package com.example.formsystem.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formsystem.R;
import com.example.formsystem.model.Answer;
import com.example.formsystem.model.Interview;
import com.example.formsystem.model.Questions;
import com.example.formsystem.ui.ViewInterviewActivity;

import java.util.ArrayList;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    public static final String QUESTION_ID = "QUESTION_ID";
    private Context context;
    private ArrayList<Questions> questionsArrayList;
    private ArrayList<Questions> answerArrayList;

    public QuestionsAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<Questions> interviewsArrayList) {
        this.questionsArrayList = interviewsArrayList;
        this.answerArrayList = interviewsArrayList;
    }

    public ArrayList<Questions> getAnswerArrayList() {
        return answerArrayList;
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
    public void onBindViewHolder(@NonNull QuestionsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            Questions questions = questionsArrayList.get(position);
            holder.textViewQuestionTitle.setText(questions.getTitle());

            /**/
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //TODO:: Action when change text in edit text
                    String questionAnswer = holder.editTextQuestion.getText().toString();
                    Answer answer = new Answer(questions.getId(), "", questionAnswer);
                    //questionsArrayList.get(position).setAnswer(answer);
                    answerArrayList.get(position).setAnswer(answer);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            };
            holder.editTextQuestion.addTextChangedListener(textWatcher);


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

    private boolean checkInput(EditText editText) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty())
            return true;
        else
            return false;
    }


}