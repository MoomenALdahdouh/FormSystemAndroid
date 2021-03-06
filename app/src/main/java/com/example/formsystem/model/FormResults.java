package com.example.formsystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FormResults {
    @SerializedName("form")
    @Expose
    private Form form;
    @SerializedName("results")
    @Expose
    private ArrayList<Form> results;

    public FormResults() {
    }

    public FormResults(Form form) {
        this.form = form;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public ArrayList<Form> getResults() {
        return results;
    }

    public void setResults(ArrayList<Form> results) {
        this.results = results;
    }
}
