package com.example.formsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.formsystem.model.Form;
import com.example.formsystem.model.Login;
import com.example.formsystem.model.Token;
import com.example.formsystem.ui.ActivitiesActivity;
import com.example.formsystem.utils.PreferenceUtils;
import com.example.formsystem.viewmodel.FormSystemViewModel;

import io.reactivex.annotations.NonNull;

public class MainActivity extends AppCompatActivity {

    private FormSystemViewModel formSystemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //formSystemViewModel = ViewModelProviders.of(this).get(FormSystemViewModel.class);
        formSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);

    }

    private void login() {
        String email = "moomenaldahdouh@gmail.com";
        String password = "123456789";
        Login login = new Login(email, password);
        formSystemViewModel.login(login);
        formSystemViewModel.tokenMutableLiveData.observe(this, new Observer<Token>() {
            @Override
            public void onChanged(Token token) {
                Toast.makeText(getApplicationContext(), token.getToken(), Toast.LENGTH_SHORT).show();
                PreferenceUtils.saveToken(token.getToken(), getApplicationContext());
            }
        });
    }


    //Get details for this movie
    private void getForm(String authToken, String id) {
        formSystemViewModel.getForms(authToken, id);
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

    public void buttonClick(View view) {
        Toast.makeText(getApplicationContext(), "TOKEN IS:" + PreferenceUtils.getToken(getApplicationContext()) + "  ID " + PreferenceUtils.getUserId(getApplicationContext()), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), ActivitiesActivity.class));
    }


}