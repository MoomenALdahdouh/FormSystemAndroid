package com.example.formsystem.db;


import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.formsystem.model.Interview;
import com.example.formsystem.model.User;

import io.reactivex.annotations.NonNull;

@Database(entities = {Interview.class}, version = 9)
public abstract class InterviewsDatabase extends RoomDatabase {
    private static InterviewsDatabase instance;

    public abstract InterviewsDao interviewsDao();

    public static synchronized InterviewsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    InterviewsDatabase.class, "interviews_database")
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
        private InterviewsDao interviewsDao;

        private PopulateDbAsyncTask(InterviewsDatabase db) {
            interviewsDao = db.interviewsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
