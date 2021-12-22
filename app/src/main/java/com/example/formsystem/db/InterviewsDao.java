package com.example.formsystem.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.formsystem.model.Interview;
import com.example.formsystem.model.User;

import java.util.List;

@Dao
public interface InterviewsDao {
    @Insert
    void insert(Interview interview);

    @Update
    void update(Interview interview);

    @Delete
    void delete(Interview interview);

    @Query("DELETE FROM interviews_table")
    void deleteAllInterviews();

    @Query("SELECT * FROM interviews_table ORDER BY created_at DESC")
    LiveData<List<Interview>> getAllInterviews();
}
