package com.example.formsystem.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.formsystem.db.QuestionsDao;
import com.example.formsystem.db.QuestionsDatabase;
import com.example.formsystem.db.UserDao;
import com.example.formsystem.db.UserDatabase;
import com.example.formsystem.model.Questions;
import com.example.formsystem.model.User;

import java.util.List;

public class QuestionsRepository {
    private QuestionsDao questionsDao;
    private LiveData<List<Questions>> allQuestions;

    public QuestionsRepository(Application application) {
        QuestionsDatabase database = QuestionsDatabase.getInstance(application);
        questionsDao = database.questionsDao();
        allQuestions = questionsDao.getAllQuestions();
    }

    public void insert(Questions questions) {
        new InsertQuestionAsyncTask(questionsDao).execute(questions);
    }

    public void update(Questions questions) {
        new UpdateQuestionAsyncTask(questionsDao).execute(questions);
    }

    public void delete(Questions questions) {
        new DeleteQuestionAsyncTask(questionsDao).execute(questions);
    }

    public void deleteAllQuestions() {
        new DeleteAllQuestionsAsyncTask(questionsDao).execute();
    }

    public LiveData<List<Questions>> getAllQuestions() {
        return allQuestions;
    }

    private static class InsertQuestionAsyncTask extends AsyncTask<Questions, Void, Void> {
        private QuestionsDao questionsDao;

        private InsertQuestionAsyncTask(QuestionsDao questionsDao) {
            this.questionsDao = questionsDao;
        }

        @Override
        protected Void doInBackground(Questions... Questions) {
            questionsDao.insert(Questions[0]);
            return null;
        }
    }

    private static class UpdateQuestionAsyncTask extends AsyncTask<Questions, Void, Void> {
        private QuestionsDao questionsDao;

        private UpdateQuestionAsyncTask(QuestionsDao questionsDao) {
            this.questionsDao = questionsDao;
        }

        @Override
        protected Void doInBackground(Questions... Questions) {
            questionsDao.update(Questions[0]);
            return null;
        }
    }

    private static class DeleteQuestionAsyncTask extends AsyncTask<Questions, Void, Void> {
        private QuestionsDao questionsDao;

        private DeleteQuestionAsyncTask(QuestionsDao questionsDao) {
            this.questionsDao = questionsDao;
        }

        @Override
        protected Void doInBackground(Questions... Questions) {
            questionsDao.delete(Questions[0]);
            return null;
        }
    }

    private static class DeleteAllQuestionsAsyncTask extends AsyncTask<Void, Void, Void> {
        private QuestionsDao questionsDao;

        private DeleteAllQuestionsAsyncTask(QuestionsDao questionsDao) {
            this.questionsDao = questionsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            questionsDao.deleteAllQuestions();
            return null;
        }
    }
}
