package com.example.formsystem.db;


import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.formsystem.model.Answer;

import io.reactivex.annotations.NonNull;

@Database(entities = {Answer.class}, version = 4)
public abstract class AnswersDatabase extends RoomDatabase {
    private static AnswersDatabase instance;

    public abstract AnswersDao answersDao();

    public static synchronized AnswersDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AnswersDatabase.class, "answers_table")
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
        private AnswersDao answersDao;

        private PopulateDbAsyncTask(AnswersDatabase db) {
            answersDao = db.answersDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
