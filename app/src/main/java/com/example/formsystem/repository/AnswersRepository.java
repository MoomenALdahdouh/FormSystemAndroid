package com.example.formsystem.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.formsystem.db.AnswersDao;
import com.example.formsystem.db.AnswersDatabase;
import com.example.formsystem.model.Answer;


import java.util.List;

public class AnswersRepository {
    private AnswersDao answersDao;
    private LiveData<List<Answer>> allAnswers;

    public AnswersRepository(Application application) {
        AnswersDatabase database = AnswersDatabase.getInstance(application);
        answersDao = database.answersDao();
        allAnswers = answersDao.getAllAnswers();
    }

    public void insert(Answer answer) {
        new InsertAnswerAsyncTask(answersDao).execute(answer);
    }

    public void update(Answer answer) {
        new UpdateAnswerAsyncTask(answersDao).execute(answer);
    }

    public void delete(Answer answer) {
        new DeleteAnswerAsyncTask(answersDao).execute(answer);
    }

    public void deleteAllAnswers() {
        new DeleteAllAnswersAsyncTask(answersDao).execute();
    }

    public LiveData<List<Answer>> getAllAnswers() {
        return allAnswers;
    }

    private static class InsertAnswerAsyncTask extends AsyncTask<Answer, Void, Void> {
        private AnswersDao answersDao;

        private InsertAnswerAsyncTask(AnswersDao answersDao) {
            this.answersDao = answersDao;
        }

        @Override
        protected Void doInBackground(Answer... Answer) {
            answersDao.insert(Answer[0]);
            return null;
        }
    }

    private static class UpdateAnswerAsyncTask extends AsyncTask<Answer, Void, Void> {
        private AnswersDao answersDao;

        private UpdateAnswerAsyncTask(AnswersDao answersDao) {
            this.answersDao = answersDao;
        }

        @Override
        protected Void doInBackground(Answer... Answer) {
            answersDao.update(Answer[0]);
            return null;
        }
    }

    private static class DeleteAnswerAsyncTask extends AsyncTask<Answer, Void, Void> {
        private AnswersDao answersDao;

        private DeleteAnswerAsyncTask(AnswersDao answersDao) {
            this.answersDao = answersDao;
        }

        @Override
        protected Void doInBackground(Answer... Answer) {
            answersDao.delete(Answer[0]);
            return null;
        }
    }

    private static class DeleteAllAnswersAsyncTask extends AsyncTask<Void, Void, Void> {
        private AnswersDao answersDao;

        private DeleteAllAnswersAsyncTask(AnswersDao answersDao) {
            this.answersDao = answersDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            answersDao.deleteAllAnswers();
            return null;
        }
    }
}
