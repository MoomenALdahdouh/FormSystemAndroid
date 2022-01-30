package com.example.formsystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AnswersResults {
    @SerializedName("answers")
    @Expose
    private ArrayList<Answer> answers;

    public AnswersResults() {
    }

    public AnswersResults(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }
}
