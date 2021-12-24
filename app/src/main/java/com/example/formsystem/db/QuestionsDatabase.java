package com.example.formsystem.db;


import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.formsystem.model.Questions;
import com.example.formsystem.model.User;

import io.reactivex.annotations.NonNull;

@Database(entities = {Questions.class}, version = 3)
public abstract class QuestionsDatabase extends RoomDatabase {
    private static QuestionsDatabase instance;

    public abstract QuestionsDao questionsDao();

    public static synchronized QuestionsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    QuestionsDatabase.class, "questions_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private QuestionsDao questionsDao;

        private PopulateDbAsyncTask(QuestionsDatabase db) {
            questionsDao = db.questionsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
