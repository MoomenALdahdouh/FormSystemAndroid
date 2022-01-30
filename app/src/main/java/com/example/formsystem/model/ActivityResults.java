package com.example.formsystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ActivityResults {
    @SerializedName("results")
    @Expose
    private ArrayList<Activity> results;

    public ActivityResults() {
    }

    public ActivityResults(ArrayList<Activity> results) {
        this.results = results;
    }

    public ArrayList<Activity> getResults() {
        return results;
    }

    public void setResults(ArrayList<Activity> results) {
        this.results = results;
    }
}
