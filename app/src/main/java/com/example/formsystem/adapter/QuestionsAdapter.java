package com.example.formsystem.adapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formsystem.R;
import com.example.formsystem.model.Answer;
import com.example.formsystem.model.Interview;
import com.example.formsystem.model.Questions;
import com.example.formsystem.ui.ViewInterviewActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    public static final String QUESTION_ID = "QUESTION_ID";
    private Context context;
    private ArrayList<Questions> questionsArrayList;
    private ArrayList<Questions> answerArrayList;
    private ArrayList<Answer> answersFromDbArrayList;
    private boolean updateInterview = false;

    public QuestionsAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<Questions> interviewsArrayList) {
        this.questionsArrayList = interviewsArrayList;
        this.answerArrayList = interviewsArrayList;
    }

    public void setAnswersFromDbList(ArrayList<Answer> answersFromDbArrayList) {
        this.answersFromDbArrayList = answersFromDbArrayList;
    }

    public void isUpdateInterview(boolean updateInterview) {
        this.updateInterview = updateInterview;
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

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            Questions questions = questionsArrayList.get(position);
            holder.textViewQuestionTitle.setText(questions.getTitle());

            if (updateInterview) {
                for (int i = 0; i < answersFromDbArrayList.size(); i++) {
                    Answer answer = answersFromDbArrayList.get(position);
                    if (answer.getQuestions_fk_id().equals(questions.getId()))
                        holder.editTextQuestion.setText(answer.getAnswer().toString());
                }
            }
            switch (questions.getType()) {
                case "0"://Text
                    break;
                case "1"://Text
                    break;
                case "2"://Number
                    holder.editTextQuestion.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.ic_baseline_123_24), null);
                    holder.editTextQuestion.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    break;
                case "3"://Calender
                    Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                    holder.editTextQuestion.setText(mYear + "/" + mMonth + "/" + mDay);

                    holder.editTextQuestion.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.ic_baseline_calendar_month_24), null);
                    holder.editTextQuestion.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            final int DRAWABLE_LEFT = 0;
                            final int DRAWABLE_TOP = 1;
                            final int DRAWABLE_RIGHT = 2;
                            final int DRAWABLE_BOTTOM = 3;

                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                if (event.getRawX() >= (holder.editTextQuestion.getRight() - holder.editTextQuestion.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                    //showCalender(holder, mYear, mMonth, mDay);
                                    DatePickerDialog mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                            String selectedDate = selectedyear + "/" + selectedmonth + "/" + selectedday;
                                            Answer answer = new Answer(questions.getId(), "", selectedDate);
                                            answerArrayList.get(position).setAnswer(answer);
                                            holder.editTextQuestion.setText(selectedDate);
                                        }
                                    }, mYear, mMonth, mDay);
                                    mDatePicker.show();
                                }
                            }
                            return false;
                        }
                    });
                   /* holder.editTextQuestion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showCalender(holder, mYear, mMonth, mDay);
                        }
                    });*/
                    break;
                case "4"://Image
                    holder.textInputLayoutQuestion.setHint("Upload Image");
                    //holder.editTextQuestion.setEnabled(false);
                    //holder.editTextQuestion.setClickable(true);
                    holder.editTextQuestion.setFocusable(false);
                    holder.editTextQuestion.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.ic_baseline_cloud_upload_24), null);
                    holder.editTextQuestion.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            final int DRAWABLE_LEFT = 0;
                            final int DRAWABLE_TOP = 1;
                            final int DRAWABLE_RIGHT = 2;
                            final int DRAWABLE_BOTTOM = 3;

                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                if (event.getRawX() >= (holder.editTextQuestion.getRight() - holder.editTextQuestion.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                    Answer answer = new Answer(questions.getId(), "", "Uploaded");
                                    answerArrayList.get(position).setAnswer(answer);
                                    holder.editTextQuestion.setText("Uploaded");
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                    holder.editTextQuestion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Answer answer = new Answer(questions.getId(), "", "Uploaded");
                            answerArrayList.get(position).setAnswer(answer);
                            holder.editTextQuestion.setText("Uploaded");
                        }
                    });
                    break;
            }

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

    private void showCalender(ViewHolder holder, int mYear, int mMonth, int mDay) {
        DatePickerDialog mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                holder.editTextQuestion.setText(selectedyear + "/" + selectedmonth + "/" + selectedday);
            }
        }, mYear, mMonth, mDay);
        //mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }

    @Override
    public int getItemCount() {
        return questionsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestionTitle;
        EditText editTextQuestion;
        TextInputLayout textInputLayoutQuestion;
        ConstraintLayout constraintLayoutInterview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestionTitle = itemView.findViewById(R.id.textViewQuestionTitle);
            editTextQuestion = itemView.findViewById(R.id.editTextQuestion);
            textInputLayoutQuestion = itemView.findViewById(R.id.textInputLayoutQuestion);
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