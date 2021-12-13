package com.example.formsystem.model;

import com.google.gson.annotations.SerializedName;

public class FormResults {
    @SerializedName("form")
    private Form form;

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
}
