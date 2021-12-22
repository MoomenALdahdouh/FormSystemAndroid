package com.example.formsystem.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.formsystem.model.Form;
import com.example.formsystem.model.User;

import java.util.List;

@Dao
public interface FormDao {
    @Insert
    void insert(Form form);

    @Update
    void update(Form form);

    @Delete
    void delete(Form form);

    @Query("DELETE FROM form_table")
    void deleteAllForms();

    @Query("SELECT * FROM form_table ORDER BY created_at DESC")
    LiveData<List<Form>> getAllForms();
}
