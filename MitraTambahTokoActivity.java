package com.example.kartasemar.activity;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kartasemar.DaftarMitra;
import com.example.kartasemar.Interface.ApiClient;
import com.example.kartasemar.LoginMitra;
import com.example.kartasemar.Model.DefaultResponse;
import com.example.kartasemar.Model.Mitra;
import com.example.kartasemar.Model.TambahToko;
import com.example.kartasemar.R;

public class MitraTambahTokoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextNamaToko, editTextAlamatToko, editTextKontakToko, editTextPemilikToko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mitra_tambah_toko);

        editTextNamaToko = findViewById(R.id.editTextEditMitraNamaToko);
        editTextAlamatToko = findViewById(R.id.editTextAlamatToko);
        editTextKontakToko = findViewById(R.id.editTextKontakToko);
        editTextPemilikToko = findViewById(R.id.editTextPemilikToko);

        findViewById(R.id.btn_tambahtoko).setOnClickListener(this);
        findViewById(R.id.btn_batal).setOnClickListener(this);
    }
    private void tambahToko(){
        String nama_toko = editTextNamaToko.getText().toString().trim();
        String alamat_toko = editTextAlamatToko.getText().toString().trim();
        String kontak_toko = editTextKontakToko.getText().toString().trim();
        String pemilik_toko = editTextPemilikToko.getText().toString().trim();
        int user_id = 36;

        Call<TambahToko> call = ApiClient
                .getInstance()
                .getApi()
                .tambahToko(nama_toko, alamat_toko, kontak_toko, pemilik_toko, user_id);

        call.enqueue(new Callback<TambahToko>() {
            @Override
            public void onResponse(Call<TambahToko> call, Response<TambahToko> response) {
                if (response.code() == 201) {

                    TambahToko dr = response.body();
                    Toast.makeText(MitraTambahTokoActivity.this, dr.getMessage(), Toast.LENGTH_LONG).show();

                } else if (response.code() == 422) {
                    Toast.makeText(MitraTambahTokoActivity.this, "User already exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TambahToko> call, Throwable t) {
                Toast.makeText(MitraTambahTokoActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tambahtoko:
                //Toast.makeText(this, editTextPassword.getText().toString(), Toast.LENGTH_SHORT).show();
                tambahToko();

                startActivity(new Intent(this, MitraActivity.class));

            case R.id.btn_batal:

                startActivity(new Intent(this, MitraActivity.class));

                break;
        }
    }
}
