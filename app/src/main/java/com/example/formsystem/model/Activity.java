package com.example.formsystem.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(tableName = "activities_table")
public class Activity {
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("subproject_fk_id")
    @Expose
    private String subproject_fk_id;
    @SerializedName("user_fk_id")
    @Expose
    private String user_fk_id;
    @SerializedName("create_by_id")
    @Expose
    private String create_by_id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("status")
    @Expose
    private String status;

    public Activity() {
    }

    public Activity(int id, String subproject_fk_id, String user_fk_id, String create_by_id, String type, String name, String description, String image, String status) {
        this.id = id;
        this.subproject_fk_id = subproject_fk_id;
        this.user_fk_id = user_fk_id;
        this.create_by_id = create_by_id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.image = image;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubproject_fk_id() {
        return subproject_fk_id;
    }

    public void setSubproject_fk_id(String subproject_fk_id) {
        this.subproject_fk_id = subproject_fk_id;
    }

    public String getUser_fk_id() {
        return user_fk_id;
    }

    public void setUser_fk_id(String user_fk_id) {
        this.user_fk_id = user_fk_id;
    }

    public String getCreate_by_id() {
        return create_by_id;
    }

    public void setCreate_by_id(String create_by_id) {
        this.create_by_id = create_by_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
