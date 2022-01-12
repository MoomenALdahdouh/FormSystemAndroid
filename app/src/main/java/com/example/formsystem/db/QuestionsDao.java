package com.example.formsystem.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.formsystem.model.Questions;
import com.example.formsystem.model.User;

import java.util.List;

@Dao
public interface QuestionsDao {
    @Insert
    void insert(Questions questions);

    @Update
    void update(Questions questions);

    @Delete
    void delete(Questions questions);

    @Query("DELETE FROM questions_table")
    void deleteAllQuestions();

    @Query("SELECT * FROM questions_table ORDER BY created_at DESC")
    LiveData<List<Questions>> getAllQuestions();
}
