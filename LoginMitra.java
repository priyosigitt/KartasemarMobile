package com.example.kartasemar;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kartasemar.Interface.ApiClient;
import com.example.kartasemar.Model.LoginMitraResponse;
import com.example.kartasemar.activity.MitraActivity;
import com.example.kartasemar.storage.SharedPrefManager;

import java.util.regex.Pattern;

public class LoginMitra extends AppCompatActivity implements View.OnClickListener {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,20}" +               //at least 4 characters and max 20 char
                    "$");
    private EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mitra);

        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);

        findViewById(R.id.btn_login);
        findViewById(R.id.btn_daftar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, MitraActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private boolean validateEmail(){
        String user_email = editTextEmail.getText().toString().trim();

        if (user_email.isEmpty()){
            editTextEmail.setError("Email tidak boleh kosong");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()){
            editTextEmail.setError("Email tidak valid!");
            return false;
        }else {
            editTextEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){
        String user_password = editTextPassword.getText().toString().trim();

        if (user_password.isEmpty()){
            editTextPassword.setError("Password tidak boleh kosong");
            return false;
        }else if (!PASSWORD_PATTERN.matcher(user_password).matches()){
            editTextPassword.setError("Password tidak valid!");
            return false;
        }else {
            editTextPassword.setError(null);
            return true;
        }
    }

    private void mitraLogin() {
        String user_password = editTextPassword.getText().toString().trim();
        String user_email = editTextEmail.getText().toString().trim();

        if (!validateEmail() | !validatePassword()){
            return;
        }

        Call<LoginMitraResponse> call = ApiClient
                .getInstance().getApi().mitraLogin(user_email, user_password);

        call.enqueue(new Callback<LoginMitraResponse>() {
            @Override
            public void onResponse(Call<LoginMitraResponse> call, Response<LoginMitraResponse> response) {
                LoginMitraResponse loginResponse = response.body();

                if (!loginResponse.isError()) {

                    SharedPrefManager.getInstance(LoginMitra.this)
                            .saveMitra(loginResponse.getUser());

                    Intent intent = new Intent(LoginMitra.this, MitraActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);


                } else {
                    Toast.makeText(LoginMitra.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginMitraResponse> call, Throwable t) {

            }
        });
    }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    mitraLogin();
                    break;
                case R.id.btn_daftar:
                    startActivity(new Intent(LoginMitra.this, DaftarMitra.class));
                    break;
            }
        }
    }