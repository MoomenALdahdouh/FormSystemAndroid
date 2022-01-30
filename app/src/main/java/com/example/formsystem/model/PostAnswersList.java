package com.example.formsystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostAnswersList {
    @SerializedName("answersList")
    @Expose
    private ArrayList<Answer> answersList;
    @SerializedName("success")
    @Expose
    private String success;

    public PostAnswersList() {
    }

    public PostAnswersList(ArrayList<Answer> answersList) {
        this.answersList = answersList;
    }

    public ArrayList<Answer> getAnswersList() {
        return answersList;
    }

    public void setAnswersList(ArrayList<Answer> answersList) {
        this.answersList = answersList;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
