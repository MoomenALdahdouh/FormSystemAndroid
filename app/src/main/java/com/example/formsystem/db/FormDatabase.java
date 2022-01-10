package com.example.formsystem.db;


import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.formsystem.model.Form;
import com.example.formsystem.model.User;

import io.reactivex.annotations.NonNull;

@Database(entities = {Form.class}, version = 9)
public abstract class FormDatabase extends RoomDatabase {
    private static FormDatabase instance;

    public abstract FormDao formDao();

    public static synchronized FormDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FormDatabase.class, "form_database")
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
        private FormDao formDao;

        private PopulateDbAsyncTask(FormDatabase db) {
            formDao = db.formDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
