package com.example.formsystem.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InterviewResults {
    @SerializedName("interviews")
    //@Expose
    private ArrayList<Interview> interviews;

    public InterviewResults() {
    }

    public InterviewResults(ArrayList<Interview> interviews) {
        this.interviews = interviews;
    }

    public ArrayList<Interview> getInterviews() {
        return interviews;
    }

    public void setInterviews(ArrayList<Interview> interviews) {
        this.interviews = interviews;
    }
}
