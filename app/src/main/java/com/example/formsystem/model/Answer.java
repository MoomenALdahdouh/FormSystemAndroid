package com.example.formsystem.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "answers_table")
public class Answer {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("questions_fk_id")
    @Expose
    private String questions_fk_id;
    @SerializedName("interview_fk_id")
    @Expose
    private String interview_fk_id;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("created_in_local")
    @Expose
    private boolean created_in_local;
    @SerializedName("update_in_local")
    @Expose
    private boolean update_in_local;


    public Answer() {
    }

    public Answer(String questions_fk_id, String interview_fk_id, String answer, String type) {
        this.questions_fk_id = questions_fk_id;
        this.interview_fk_id = interview_fk_id;
        this.answer = answer;
        this.type = type;
    }

    public Answer(int id, String questions_fk_id, String interview_fk_id, String answer, String type, boolean created_in_local, boolean update_in_local) {
        this.id = id;
        this.questions_fk_id = questions_fk_id;
        this.interview_fk_id = interview_fk_id;
        this.answer = answer;
        this.type = type;
        this.created_in_local = created_in_local;
        this.update_in_local = update_in_local;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestions_fk_id() {
        return questions_fk_id;
    }

    public void setQuestions_fk_id(String questions_fk_id) {
        this.questions_fk_id = questions_fk_id;
    }

    public String getInterview_fk_id() {
        return interview_fk_id;
    }

    public void setInterview_fk_id(String interview_fk_id) {
        this.interview_fk_id = interview_fk_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isCreated_in_local() {
        return created_in_local;
    }

    public void setCreated_in_local(boolean created_in_local) {
        this.created_in_local = created_in_local;
    }

    public boolean isUpdate_in_local() {
        return update_in_local;
    }

    public void setUpdate_in_local(boolean update_in_local) {
        this.update_in_local = update_in_local;
    }
}
