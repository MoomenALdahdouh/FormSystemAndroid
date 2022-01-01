package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.example.formsystem.MainActivity;
import com.example.formsystem.R;
import com.example.formsystem.utils.PreferenceUtils;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setUpLanguage(PreferenceUtils.getLanguage(getApplicationContext()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!PreferenceUtils.getToken(SplashActivity.this).isEmpty()) { 
                    startActivity(new Intent(SplashActivity.this, WorkerFormsActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 3000);
    }

    private void setUpLanguage(String language) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(language));
        resources.updateConfiguration(configuration, displayMetrics);
        PreferenceUtils.saveLanguage(language, getApplicationContext());
    }
}