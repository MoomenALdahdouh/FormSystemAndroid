package com.example.formsystem.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionsResults {
    @SerializedName("questions")
    //@Expose
    private ArrayList<Questions> questions;

    public QuestionsResults(ArrayList<Questions> questions) {
        this.questions = questions;
    }

    public ArrayList<Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Questions> questions) {
        this.questions = questions;
    }
}
