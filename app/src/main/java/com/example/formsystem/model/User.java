package com.example.formsystem.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    private int id;
    @SerializedName("project_fk_id")
    private String project_fk_id;
    @SerializedName("create_by_id")
    private String create_by_id;
    @SerializedName("name")
    private String name;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("location")
    private String location;
    @SerializedName("email_verified_at")
    private String email_verified_at;
    @SerializedName("password")
    private String password;
    @SerializedName("two_factor_secret")
    private String two_factor_secret;
    @SerializedName("two_factor_recovery_codes")
    private String two_factor_recovery_codes;
    @SerializedName("remember_token")
    private String remember_token;
    @SerializedName("current_team_id")
    private String current_team_id;
    @SerializedName("profile_photo_path")
    private String profile_photo_path;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("status")
    private String status;
    @SerializedName("type")
    private String type;


    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Ignore
    public User(int id, String project_fk_id, String create_by_id, String name, String nickname
            , String email, String phone, String location, String email_verified_at, String password
            , String two_factor_secret, String two_factor_recovery_codes, String remember_token, String current_team_id
            , String profile_photo_path, String created_at, String updated_at, String status, String type) {
        this.id = id;
        this.project_fk_id = project_fk_id;
        this.create_by_id = create_by_id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.email_verified_at = email_verified_at;
        this.password = password;
        this.two_factor_secret = two_factor_secret;
        this.two_factor_recovery_codes = two_factor_recovery_codes;
        this.remember_token = remember_token;
        this.current_team_id = current_team_id;
        this.profile_photo_path = profile_photo_path;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProject_fk_id() {
        return project_fk_id;
    }

    public void setProject_fk_id(String project_fk_id) {
        this.project_fk_id = project_fk_id;
    }

    public String getCreate_by_id() {
        return create_by_id;
    }

    public void setCreate_by_id(String create_by_id) {
        this.create_by_id = create_by_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTwo_factor_secret() {
        return two_factor_secret;
    }

    public void setTwo_factor_secret(String two_factor_secret) {
        this.two_factor_secret = two_factor_secret;
    }

    public String getTwo_factor_recovery_codes() {
        return two_factor_recovery_codes;
    }

    public void setTwo_factor_recovery_codes(String two_factor_recovery_codes) {
        this.two_factor_recovery_codes = two_factor_recovery_codes;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }

    public String getCurrent_team_id() {
        return current_team_id;
    }

    public void setCurrent_team_id(String current_team_id) {
        this.current_team_id = current_team_id;
    }

    public String getProfile_photo_path() {
        return profile_photo_path;
    }

    public void setProfile_photo_path(String profile_photo_path) {
        this.profile_photo_path = profile_photo_path;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
