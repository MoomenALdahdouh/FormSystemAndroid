package com.example.formsystem.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.formsystem.db.InterviewsDao;
import com.example.formsystem.db.InterviewsDatabase;
import com.example.formsystem.db.UserDatabase;
import com.example.formsystem.model.Interview;
import com.example.formsystem.model.User;

import java.util.List;

public class InterviewsRepository {
    private InterviewsDao interviewsDao;
    private LiveData<List<Interview>> allInterviews;

    public InterviewsRepository(Application application) {
        InterviewsDatabase database = InterviewsDatabase.getInstance(application);
        interviewsDao = database.interviewsDao();
        allInterviews = interviewsDao.getAllInterviews();
    }

    public void insert(Interview interview) {
        new InsertUserAsyncTask(interviewsDao).execute(interview);
    }

    public void update(Interview interview) {
        new UpdateUserAsyncTask(interviewsDao).execute(interview);
    }

    public void delete(Interview interview) {
        new DeleteUserAsyncTask(interviewsDao).execute(interview);
    }

    public void deleteAllInterviews() {
        new DeleteAllUsersAsyncTask(interviewsDao).execute();
    }

    public LiveData<List<Interview>> getAllInterviews() {
        return allInterviews;
    }

    private static class InsertUserAsyncTask extends AsyncTask<Interview, Void, Void> {
        private InterviewsDao interviewsDao;

        private InsertUserAsyncTask(InterviewsDao interviewsDao) {
            this.interviewsDao = interviewsDao;
        }

        @Override
        protected Void doInBackground(Interview... Interview) {
            interviewsDao.insert(Interview[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<Interview, Void, Void> {
        private InterviewsDao interviewsDao;

        private UpdateUserAsyncTask(InterviewsDao interviewsDao) {
            this.interviewsDao = interviewsDao;
        }

        @Override
        protected Void doInBackground(Interview... Interview) {
            interviewsDao.update(Interview[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<Interview, Void, Void> {
        private InterviewsDao interviewsDao;

        private DeleteUserAsyncTask(InterviewsDao interviewsDao) {
            this.interviewsDao = interviewsDao;
        }

        @Override
        protected Void doInBackground(Interview... Interview) {
            interviewsDao.delete(Interview[0]);
            return null;
        }
    }

    private static class DeleteAllUsersAsyncTask extends AsyncTask<Void, Void, Void> {
        private InterviewsDao interviewsDao;

        private DeleteAllUsersAsyncTask(InterviewsDao interviewsDao) {
            this.interviewsDao = interviewsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            interviewsDao.deleteAllInterviews();
            return null;
        }
    }
}
