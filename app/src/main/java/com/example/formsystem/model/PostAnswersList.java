package com.example.formsystem.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostAnswersList {
    @SerializedName("answersList")
    private ArrayList<Answer> answersList;
    @SerializedName("success")
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
