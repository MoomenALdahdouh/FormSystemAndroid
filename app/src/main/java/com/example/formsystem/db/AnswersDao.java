package com.example.formsystem.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.formsystem.model.Answer;

import java.util.List;

@Dao
public interface AnswersDao {
    @Insert
    void insert(Answer answer);

    @Update
    void update(Answer answer);

    @Delete
    void delete(Answer answer);

    @Query("DELETE FROM answers_table")
    void deleteAllAnswers();

    @Query("SELECT * FROM answers_table ORDER BY created_at DESC")
    LiveData<List<Answer>> getAllAnswers();
}
