package com.example.formsystem.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.formsystem.model.Activity;
import com.example.formsystem.model.User;

import java.util.List;

@Dao
public interface ActivitiesDao {
    @Insert
    void insert(Activity activity);

    @Update
    void update(Activity activity);

    @Delete
    void delete(Activity activity);

    @Query("DELETE FROM activities_table")
    void deleteAllActivities();

    @Query("SELECT * FROM activities_table")//ORDER BY created_at DESC
    LiveData<List<Activity>> getAllActivities();
}
