package com.example.formsystem.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.formsystem.R;
import com.example.formsystem.model.Answer;
import com.example.formsystem.model.Interview;
import com.example.formsystem.model.Questions;
import com.example.formsystem.ui.MakeInterviewActivity;
import com.example.formsystem.ui.ViewImageActivity;
import com.example.formsystem.ui.ViewInterviewActivity;
import com.example.formsystem.utils.PreferenceUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import id.zelory.compressor.Compressor;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    public static final String QUESTION_ID = "QUESTION_ID";
    public static final String IMAGE_NAME = "IMAGE_NAME";
    private Context context;
    private ArrayList<Questions> questionsArrayList;
    private ArrayList<Questions> answerArrayList;
    private ArrayList<Answer> answersFromDbArrayList;
    private boolean updateInterview = false;
    private boolean isLocal = false;
    //Upload image

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

    public void setLocal(boolean local) {
        isLocal = local;
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

    public String stringFromObject(Answer answer) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(answer);
        return jsonString;
    }

    public Answer getObjectFromString(String jsonString) {
        Type ansType = new TypeToken<Answer>() {
        }.getType();
        Answer answer = new Gson().fromJson(jsonString, ansType);
        return answer;
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            Questions questions = questionsArrayList.get(position);
            holder.textViewQuestionTitle.setText(questions.getTitle());
            String questionId = String.valueOf(questions.getId());

            /*if (updateInterview) {//If update interview fill answer

             *//*for (int i = 0; i < answersFromDbArrayList.size(); i++) {
                    Answer answer = answersFromDbArrayList.get(position);
                    String question_fk_id = answer.getQuestions_fk_id();
                    if (questionId.equals(question_fk_id)) {
                        Log.d("postionQuestion","answer.getAnswer()"+answer.getAnswer());
                        holder.editTextQuestion.setText(answer.getAnswer().toString());
                    }
                }*//*
                //Disable edit text
                //holder.editTextQuestion.setEnabled(false);
                holder.editTextQuestion.setClickable(false);
                holder.editTextQuestion.setFocusable(false);
            }*/
            switch (questions.getType()) {//Custom design edit text with question type
                case "0"://Text
                    break;
                case "1"://Text
                    break;
                case "2"://Number
                    holder.editTextQuestion.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.ic_baseline_123_24), null);
                    holder.editTextQuestion.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    break;
                case "3"://Calender
                    holder.editTextQuestion.setInputType(InputType.TYPE_CLASS_NUMBER);
                    Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH) + 1;
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

                                    DatePickerDialog mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                            Answer old_answer = getObjectFromString(questions.getAnswer());
                                            String selectedDate = selectedyear + "/" + (selectedmonth + 1) + "/" + selectedday;
                                            String old_id = String.valueOf(old_answer.getId());
                                            int id;
                                            if (updateInterview && !old_id.equals("0"))
                                                id = old_answer.getId();
                                            else
                                                id = (int) System.currentTimeMillis() * -1;

                                            Answer answer = new Answer(id, String.valueOf(questions.getId()), "", selectedDate, questions.getType(), isLocal, false);
                                            answerArrayList.get(position).setAnswer(stringFromObject(answer));
                                            holder.editTextQuestion.setText(selectedDate);
                                        }
                                    }, mYear, mMonth - 1, mDay);
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
                    holder.textInputLayoutQuestion.setHint("Image");
                    //holder.editTextQuestion.setEnabled(false);
                    holder.editTextQuestion.setClickable(true);
                    holder.editTextQuestion.setFocusable(false);
                    if (updateInterview) {
                        holder.editTextQuestion.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.ic_baseline_preview_24), null, ContextCompat.getDrawable(context, R.drawable.ic_baseline_cloud_upload_24), null);
                    } else
                        holder.editTextQuestion.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.ic_baseline_cloud_upload_24), null);
                    holder.editTextQuestion.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            final int DRAWABLE_LEFT = 0;
                            final int DRAWABLE_TOP = 1;
                            final int DRAWABLE_RIGHT = 2;
                            final int DRAWABLE_BOTTOM = 3;

                            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_BUTTON_PRESS) {
                                if (event.getRawX() >= (holder.editTextQuestion.getRight() - holder.editTextQuestion.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                    Answer old_answer = getObjectFromString(questions.getAnswer());
                                    Answer answer;
                                    int id;
                                    /*Upload image*/
                                    String old_id = String.valueOf(old_answer.getId());
                                    if (updateInterview && !old_id.equals("0")) {
                                        id = old_answer.getId();
                                        answer = new Answer(id, String.valueOf(questions.getId()), "", old_answer.getAnswer(), questions.getType(), isLocal, false);
                                    } else {
                                        id = (int) System.currentTimeMillis() * -1;
                                        answer = new Answer(id, String.valueOf(questions.getId()), "", old_answer.getAnswer(), questions.getType(), isLocal, false);
                                    }
                                    answerArrayList.get(position).setAnswer(stringFromObject(answer));
                                    if (updateInterview) {
                                        ((ViewInterviewActivity) context).setQuestionId(String.valueOf(questions.getId()));
                                    } else
                                        ((MakeInterviewActivity) context).setQuestionId(String.valueOf(questions.getId()));
                                    Log.d("postionQuestion", "postionQuestion" + position);
                                    cropImage();
                                    /**/
                                    return true;
                                } else {
                                    if (updateInterview)
                                        if (event.getRawX() >= (holder.editTextQuestion.getLeft() + holder.editTextQuestion.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                                            //View Image
                                            String answerString = questionsArrayList.get(position).getAnswer();
                                            Answer answer = getObjectFromString(answerString);
                                            Intent intent = new Intent(context, ViewImageActivity.class);
                                            intent.putExtra(IMAGE_NAME, "image");
                                            PreferenceUtils.saveImage(answer.getAnswer().toString(), context);
                                            Log.d("imageName", PreferenceUtils.getImage(context));
                                            context.startActivity(intent);
                                        }
                                }
                            }
                            return false;
                        }
                    });
                    /*holder.editTextQuestion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Answer answer = new Answer(questions.getId(), "", "Uploaded");
                            answerArrayList.get(position).setAnswer(answer);
                            holder.editTextQuestion.setText("Uploaded");
                        }
                    });*/
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
                    if (updateInterview) {
                        Answer answer = getObjectFromString(questions.getAnswer());
                        if (answer.getId() > 0 || !answer.getType().isEmpty()) {
                            answer.setAnswer(questionAnswer);
                        } else {
                            int id = (int) System.currentTimeMillis() * -1;
                            answer = new Answer(id, String.valueOf(questions.getId()), "", questionAnswer, questions.getType(), isLocal, false);
                        }
                        answerArrayList.get(position).setAnswer(stringFromObject(answer));
                        /*Answer answer = getObjectFromString(questions.getAnswer());
                        answer.setAnswer(questionAnswer);
                        answerArrayList.get(position).setAnswer(stringFromObject(answer));*/
                    } else {
                        int id = (int) System.currentTimeMillis() * -1;
                        Answer answer = new Answer(id, String.valueOf(questions.getId()), "", questionAnswer, questions.getType(), isLocal, false);
                        answerArrayList.get(position).setAnswer(stringFromObject(answer));
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            };
            holder.editTextQuestion.addTextChangedListener(textWatcher);
            if (updateInterview) {
                Answer answer = getObjectFromString(questions.getAnswer());
                if (answer.getType().equals("4")) {
                    if (isLocal && answer.getAnswer().length() > 30) {
                        int id = (int) System.currentTimeMillis() * -1;
                        holder.editTextQuestion.setText(id + ".jpg");
                    } else
                        holder.editTextQuestion.setText(answer.getAnswer());
                } else
                    holder.editTextQuestion.setText(answer.getAnswer());
            }

        } catch (Exception exception) {

        }
    }

    public static String randomFakeName() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(15);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    private void cropImage() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        } else {
            //TODO: Must add activity on Manifest file add this line code
            //            AndroidManifest.xml
            //            <activity
            //            android:name="com.theartofdev.edmodo.cropper.CropImageActivity"
            //            android:theme="@style/Base.Theme.AppCompat" />
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    //.setMinCropResultSize(512,512)
                    .setBackgroundColor(Color.parseColor("#00000000"))
                    .setAspectRatio(4, 3)
                    .start((Activity) context);
        }
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