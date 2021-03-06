package com.example.formsystem.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.formsystem.MainActivity;
import com.example.formsystem.databinding.ActivityLoginBinding;
import com.example.formsystem.model.Login;
import com.example.formsystem.model.Token;
import com.example.formsystem.model.User;
import com.example.formsystem.utils.PreferenceUtils;
import com.example.formsystem.viewmodel.FormSystemViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FormSystemViewModel formSystemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        formSystemViewModel = new ViewModelProvider(this).get(FormSystemViewModel.class);
        loginButtonClick();
    }

    private void loginButtonClick() {
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.linearProgressLogin.setVisibility(View.VISIBLE);
                String email = binding.editTextEmail.getText().toString().trim();
                String password = binding.editTextPassword.getText().toString().trim();
                Login login = new Login(email, password);
                formSystemViewModel.login(login);
                formSystemViewModel.tokenMutableLiveData.observe(LoginActivity.this, new Observer<Token>() {
                    @Override
                    public void onChanged(Token token) {
                        String tokenResponse = token.getToken();
                        User user = token.getUser();
                        //Log.d("user.getId","User Id::"+user.getId());
                        binding.linearProgressLogin.setVisibility(View.GONE);
                        if (tokenResponse != null) {
                            PreferenceUtils.saveToken(tokenResponse, getApplicationContext());
                            PreferenceUtils.saveUserId(String.valueOf(user.getId()), getApplicationContext());
                            Toast.makeText(getApplicationContext(), "Successfully login", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, WorkerFormsActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed login! try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}