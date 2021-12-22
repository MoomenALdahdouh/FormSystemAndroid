package com.example.formsystem.db;


import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.formsystem.model.Activity;
import com.example.formsystem.model.User;

import io.reactivex.annotations.NonNull;

@Database(entities = {Activity.class}, version = 2)
public abstract class ActivitiesDatabase extends RoomDatabase {
    private static ActivitiesDatabase instance;

    public abstract ActivitiesDao activitiesDao();

    public static synchronized ActivitiesDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ActivitiesDatabase.class, "activities_database")
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
        private ActivitiesDao activitiesDao;

        private PopulateDbAsyncTask(ActivitiesDatabase db) {
            activitiesDao = db.activitiesDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
