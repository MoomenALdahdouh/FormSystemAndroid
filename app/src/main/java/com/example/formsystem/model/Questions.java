package com.example.formsystem.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Questions {
    @SerializedName("id")
    private String id;
    @SerializedName("form_fk_id")
    private String form_fk_id;
    @SerializedName("questions_key")
    private String questions_key;
    @SerializedName("title")
    private String title;
    @SerializedName("body")
    private String body;
    @SerializedName("type")
    private String type;

    public Questions() {
    }

    public Questions(String id, String form_fk_id, String questions_key, String title, String body, String type) {
        this.id = id;
        this.form_fk_id = form_fk_id;
        this.questions_key = questions_key;
        this.title = title;
        this.body = body;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForm_fk_id() {
        return form_fk_id;
    }

    public void setForm_fk_id(String form_fk_id) {
        this.form_fk_id = form_fk_id;
    }

    public String getQuestions_key() {
        return questions_key;
    }

    public void setQuestions_key(String questions_key) {
        this.questions_key = questions_key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
