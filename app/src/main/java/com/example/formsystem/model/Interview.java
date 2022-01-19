package com.example.formsystem.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(tableName = "interviews_table")
public class Interview {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int id;
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
    @SerializedName("interview_id")
    private String interview_id;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("worker_fk_id")
    private String worker_fk_id;
    @SerializedName("created_in_local")
    private boolean created_in_local;

    public Interview() {
    }

    public Interview(String form_fk_id, String title, String customer_location, String latitude
            , String longitude, String worker_fk_id) {
        this.form_fk_id = form_fk_id;
        this.title = title;
        this.customer_location = customer_location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.worker_fk_id = worker_fk_id;
    }

    public Interview(int id, String form_fk_id, String title, String customer_location, String latitude
            , String longitude, String worker_fk_id, String created_at, boolean created_in_local) {
        this.id = id;
        this.form_fk_id = form_fk_id;
        this.title = title;
        this.customer_location = customer_location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.worker_fk_id = worker_fk_id;
        this.created_at = created_at;
        this.created_in_local = created_in_local;
    }

    /*public Interview(int id, String form_fk_id, String title, String customer_location, String latitude
            , String longitude, String worker_fk_id, String created_at) {
        this.id = id;
        this.form_fk_id = form_fk_id;
        this.title = title;
        this.customer_location = customer_location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.worker_fk_id = worker_fk_id;
        this.created_at = created_at;
    }*/

    public Interview(String form_fk_id, String title, String customer_location) {
        this.form_fk_id = form_fk_id;
        this.title = title;
        this.customer_location = customer_location;
    }

    /*public Interview(int id, String form_fk_id, String title, String created_at, String updated_at, String status, String customer_location) {
        this.id = id;
        this.form_fk_id = form_fk_id;
        this.title = title;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
        this.customer_location = customer_location;
    }*/

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

    public String getInterview_id() {
        return interview_id;
    }

    public void setInterview_id(String interview_id) {
        this.interview_id = interview_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getWorker_fk_id() {
        return worker_fk_id;
    }

    public void setWorker_fk_id(String worker_fk_id) {
        this.worker_fk_id = worker_fk_id;
    }

    public boolean isCreated_in_local() {
        return created_in_local;
    }

    public void setCreated_in_local(boolean created_in_local) {
        this.created_in_local = created_in_local;
    }
}
