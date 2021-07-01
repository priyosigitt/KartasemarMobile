package com.example.kartasemar;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kartasemar.Interface.ApiClient;
import com.example.kartasemar.Model.DefaultResponse;
import com.example.kartasemar.storage.SharedPrefManager;

public class DaftarMitra  extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName, editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_mitra);

        editTextName = findViewById(R.id.editTextPersonName);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);


        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.masuk).setOnClickListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(this, LoginMitra.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void userSignUp() {
        String username = editTextName.getText().toString().trim();
        String user_email = editTextEmail.getText().toString().trim();
        String user_password = editTextPassword.getText().toString().trim();

        if (user_email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (user_password.isEmpty()) {
            editTextPassword.setError("Password required");
            editTextPassword.requestFocus();
            return;
        }

        if (user_password.length() < 6) {
            editTextPassword.setError("Password should be atleast 6 character long");
            editTextPassword.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            editTextName.setError("Name required");
            editTextName.requestFocus();
            return;
        }


        Call<DefaultResponse> call = ApiClient
                .getInstance()
                .getApi()
                .daftarMitra(username, user_email, user_password);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.code() == 201) {

                    DefaultResponse dr = response.body();
                    Toast.makeText(DaftarMitra.this, dr.getMsg(), Toast.LENGTH_LONG).show();

                } else if (response.code() == 422) {
                    Toast.makeText(DaftarMitra.this, "User already exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

                Toast.makeText(DaftarMitra.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                //Toast.makeText(this, editTextPassword.getText().toString(), Toast.LENGTH_SHORT).show();
                userSignUp();

                startActivity(new Intent(this, LoginMitra.class));

            case R.id.masuk:

                startActivity(new Intent(this, LoginMitra.class));

                break;
        }
    }
}
