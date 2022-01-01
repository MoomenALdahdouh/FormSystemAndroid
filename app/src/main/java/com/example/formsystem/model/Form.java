package com.example.formsystem.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "form_table")
public class Form {
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("activity_fk_id")
    @Expose
    private String activity_fk_id;
    @SerializedName("user_fk_id")
    @Expose
    private String user_fk_id;
    @SerializedName("subproject_fk_id")
    @Expose
    private String subproject_fk_id;
    @SerializedName("project_fk_id")
    @Expose
    private String project_fk_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("describtion")
    @Expose
    private String describtion;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("update_at")
    @Expose
    private String update_at;

    public Form() {
    }

    public Form(int id, String activity_fk_id, String user_fk_id, String subproject_fk_id
            , String project_fk_id, String name, String describtion, String image, String status, String created_at, String update_at) {
        this.id = id;
        this.activity_fk_id = activity_fk_id;
        this.user_fk_id = user_fk_id;
        this.subproject_fk_id = subproject_fk_id;
        this.project_fk_id = project_fk_id;
        this.name = name;
        this.describtion = describtion;
        this.image = image;
        this.status = status;
        this.created_at = created_at;
        this.update_at = update_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivity_fk_id() {
        return activity_fk_id;
    }

    public void setActivity_fk_id(String activity_fk_id) {
        this.activity_fk_id = activity_fk_id;
    }

    public String getUser_fk_id() {
        return user_fk_id;
    }

    public void setUser_fk_id(String user_fk_id) {
        this.user_fk_id = user_fk_id;
    }

    public String getSubproject_fk_id() {
        return subproject_fk_id;
    }

    public void setSubproject_fk_id(String subproject_fk_id) {
        this.subproject_fk_id = subproject_fk_id;
    }

    public String getProject_fk_id() {
        return project_fk_id;
    }

    public void setProject_fk_id(String project_fk_id) {
        this.project_fk_id = project_fk_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }
}
