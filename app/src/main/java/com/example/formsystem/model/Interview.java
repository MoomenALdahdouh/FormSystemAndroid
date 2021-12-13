package com.example.formsystem.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Interview {
    @SerializedName("id")
    private String id;
    @SerializedName("form_fk_id")
    private String form_fk_id;
    @SerializedName("title")
    private String title;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("status")
    private String status;
    @SerializedName("customer_location")
    private String customer_location;

    public Interview() {
    }

    public Interview(String id, String form_fk_id, String title, String created_at, String updated_at, String status, String customer_location) {
        this.id = id;
        this.form_fk_id = form_fk_id;
        this.title = title;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
        this.customer_location = customer_location;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomer_location() {
        return customer_location;
    }

    public void setCustomer_location(String customer_location) {
        this.customer_location = customer_location;
    }
}
