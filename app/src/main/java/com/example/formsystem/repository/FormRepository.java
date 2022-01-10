package com.example.formsystem.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.formsystem.db.FormDao;
import com.example.formsystem.db.FormDatabase;
import com.example.formsystem.db.UserDao;
import com.example.formsystem.db.UserDatabase;
import com.example.formsystem.model.Form;
import com.example.formsystem.model.User;

import java.util.List;

public class FormRepository {
    private FormDao formDao;
    private LiveData<List<Form>> allForms;

    public FormRepository(Application application) {
        FormDatabase database = FormDatabase.getInstance(application);
        formDao = database.formDao();
        allForms = formDao.getAllForms();
    }

    public void insert(Form form) {
        new InsertUserAsyncTask(formDao).execute(form);
    }

    public void update(Form form) {
        new UpdateUserAsyncTask(formDao).execute(form);
    }

    public void delete(Form form) {
        new DeleteUserAsyncTask(formDao).execute(form);
    }

    public void deleteAllForms() {
        new DeleteAllFormsAsyncTask(formDao).execute();
    }

    public LiveData<List<Form>> getAllForms() {
        return allForms;
    }

    private static class InsertUserAsyncTask extends AsyncTask<Form, Void, Void> {
        private FormDao formDao;

        private InsertUserAsyncTask(FormDao formDao) {
            this.formDao = formDao;
        }

        @Override
        protected Void doInBackground(Form... Form) {
            formDao.insert(Form[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<Form, Void, Void> {
        private FormDao formDao;

        private UpdateUserAsyncTask(FormDao formDao) {
            this.formDao = formDao;
        }

        @Override
        protected Void doInBackground(Form... Form) {
            formDao.update(Form[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<Form, Void, Void> {
        private FormDao formDao;

        private DeleteUserAsyncTask(FormDao formDao) {
            this.formDao = formDao;
        }

        @Override
        protected Void doInBackground(Form... Form) {
            formDao.delete(Form[0]);
            return null;
        }
    }

    private static class DeleteAllFormsAsyncTask extends AsyncTask<Void, Void, Void> {
        private FormDao formDao;

        private DeleteAllFormsAsyncTask(FormDao formDao) {
            this.formDao = formDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            formDao.deleteAllForms();
            return null;
        }
    }
}
