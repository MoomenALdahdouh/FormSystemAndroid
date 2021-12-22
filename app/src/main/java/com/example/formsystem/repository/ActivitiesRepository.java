package com.example.formsystem.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.formsystem.db.ActivitiesDao;
import com.example.formsystem.db.ActivitiesDatabase;
import com.example.formsystem.model.Activity;
import com.example.formsystem.model.User;

import java.util.List;

public class ActivitiesRepository {
    private ActivitiesDao ActivitiesDao;
    private LiveData<List<Activity>> allActivities;

    public ActivitiesRepository(Application application) {
        ActivitiesDatabase database = ActivitiesDatabase.getInstance(application);
        ActivitiesDao = database.activitiesDao();
        allActivities = ActivitiesDao.getAllActivities();
    }

    public void insert(Activity activity) {
        new InsertUserAsyncTask(ActivitiesDao).execute(activity);
    }

    public void update(Activity activity) {
        new UpdateUserAsyncTask(ActivitiesDao).execute(activity);
    }

    public void delete(Activity activity) {
        new DeleteUserAsyncTask(ActivitiesDao).execute(activity);
    }

    public void deleteAllUsers() {
        new DeleteAllUsersAsyncTask(ActivitiesDao).execute();
    }

    public LiveData<List<Activity>> getAllActivities() {
        return allActivities;
    }

    private static class InsertUserAsyncTask extends AsyncTask<Activity, Void, Void> {
        private ActivitiesDao activitiesDao;

        private InsertUserAsyncTask(ActivitiesDao ActivitiesDao) {
            this.activitiesDao = ActivitiesDao;
        }

        @Override
        protected Void doInBackground(Activity... Activity) {
            activitiesDao.insert(Activity[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<Activity, Void, Void> {
        private ActivitiesDao activitiesDao;

        private UpdateUserAsyncTask(ActivitiesDao activitiesDao) {
            this.activitiesDao = activitiesDao;
        }

        @Override
        protected Void doInBackground(Activity... Activity) {
            activitiesDao.update(Activity[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<Activity, Void, Void> {
        private ActivitiesDao activitiesDao;

        private DeleteUserAsyncTask(ActivitiesDao ActivitiesDao) {
            this.activitiesDao = ActivitiesDao;
        }

        @Override
        protected Void doInBackground(Activity... Activity) {
            activitiesDao.delete(Activity[0]);
            return null;
        }
    }

    private static class DeleteAllUsersAsyncTask extends AsyncTask<Void, Void, Void> {
        private ActivitiesDao activitiesDao;

        private DeleteAllUsersAsyncTask(ActivitiesDao activitiesDao) {
            this.activitiesDao = activitiesDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            activitiesDao.deleteAllActivities();
            return null;
        }
    }
}
