package com.example.formsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.example.formsystem.model.Form;
import com.example.formsystem.viewmodel.FormSystemViewModel;

public class MainActivity extends AppCompatActivity {

    private FormSystemViewModel formSystemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //formSystemViewModel = ViewModelProviders.of(this).get(FormSystemViewModel.class);
        formSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);

    }


    //Get details for this movie
    private void getForm(String id) {
        formSystemViewModel.getForms(id);
        formSystemViewModel.formsMutableLiveData.observe(this, new Observer<Form>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(Form form) {
                try {

                } catch (NullPointerException e) {
                    Log.d("e", e.getMessage());
                }
            }
        });
    }
}