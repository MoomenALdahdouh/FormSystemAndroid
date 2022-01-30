package com.example.formsystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserResults {
    @SerializedName("user")
    @Expose
    private User user;

    public UserResults() {
    }

    public UserResults(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
