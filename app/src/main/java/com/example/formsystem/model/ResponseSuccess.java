package com.example.formsystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSuccess {
    @SerializedName("success")
    @Expose
    private String success;

    public ResponseSuccess(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
