package com.example.formsystem.model;

import com.google.gson.annotations.SerializedName;

public class Answer {
    @SerializedName("id")
    private String id;
    @SerializedName("questions_fk_id")
    private String questions_fk_id;
    @SerializedName("interview_fk_id")
    private String interview_fk_id;
    @SerializedName("answer")
    private String answer;


    public Answer() {
    }

    public Answer(String questions_fk_id, String interview_fk_id, String answer) {
        this.questions_fk_id = questions_fk_id;
        this.interview_fk_id = interview_fk_id;
        this.answer = answer;
    }

    public Answer(String id, String questions_fk_id, String interview_fk_id, String answer) {
        this.id = id;
        this.questions_fk_id = questions_fk_id;
        this.interview_fk_id = interview_fk_id;
        this.answer = answer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
