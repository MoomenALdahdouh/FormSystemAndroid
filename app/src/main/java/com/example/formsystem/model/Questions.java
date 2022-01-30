package com.example.formsystem.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(tableName = "questions_table")
public class Questions {
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("form_fk_id")
    @Expose
    private String form_fk_id;
    @SerializedName("questions_key")
    @Expose
    private String questions_key;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    /*User answer field to save question answer in adapter*/
    //@Ignore
    @SerializedName("answer")
    @Expose
    private String answer = stringFromObject();

    public Questions() {
    }

    public String stringFromObject() {
        //int id = (int) System.currentTimeMillis();
        Answer answer = new Answer(0, "", "", "", "",false,false);
        Gson gson = new Gson();
        return gson.toJson(answer);
    }

    public Questions(int id, String form_fk_id, String questions_key, String title, String body, String type) {
        this.id = id;
        this.form_fk_id = form_fk_id;
        this.questions_key = questions_key;
        this.title = title;
        this.body = body;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
