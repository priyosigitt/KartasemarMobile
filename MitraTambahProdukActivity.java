package com.example.kartasemar.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kartasemar.Interface.ApiClient;
import com.example.kartasemar.LoginMitra;
import com.example.kartasemar.Model.TambahProduk;
import com.example.kartasemar.R;
import com.example.kartasemar.storage.SharedPrefManager;
import com.squareup.picasso.Picasso;

public class MitraTambahProdukActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText, editTextNamaProduk, editTextDeskripsi, editTextharga, editTextJumlah;
    TextView textView;
    RadioButton rbRajut, rbBatik, rbDaurulang;
    private int kategori = 0;
    private  static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    ImageView pilihfoto, previewfoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mitra_tambah);

        editTextNamaProduk = findViewById(R.id.editTextEditMitraNamaProduk);
        editTextDeskripsi = findViewById(R.id.editTextDeskripsi);
        editTextharga = findViewById(R.id.editTextHarga);
        editTextJumlah = findViewById(R.id.editTextJumlah);
        previewfoto = findViewById(R.id.previewfoto);
        pilihfoto = findViewById(R.id.imageView27);

        rbBatik = findViewById(R.id.radioButtonBatik);
        rbRajut = findViewById(R.id.radioButtonRajut);
        rbDaurulang = findViewById(R.id.radioButtonDaurulang);

        rbBatik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategori = 79;
            }
        });

        rbRajut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategori = 87;
            }
        });

        rbDaurulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategori = 86;
            }
        });

        editText = findViewById(R.id.editTextEditMitraNamaProduk);
        textView = findViewById(R.id.textviewcount);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String currentText = editable.toString();
                int currentLength = currentText.length();
                textView.setText(currentLength+"/100");
            }
        });

        pilihfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        findViewById(R.id.btn_tambahproduk).setOnClickListener(this);
        findViewById(R.id.btn_batal).setOnClickListener(this);
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

    private void tambahProduk() {
        int user_id = 37;
        String nama_produk = editTextNamaProduk.getText().toString().trim();
        String deskripsi_produk = editTextDeskripsi.getText().toString().trim();
        int sub_kategori_produk = 47;
        int toko_produk = 15;
        int ketersediaan_produk = 1;
        int status_produk = 1;
        String harga = editTextharga.getText().toString().trim();
        int harga_produk = Integer.parseInt(harga);
        int berat_produk = 1000;
        String jumlah = editTextharga.getText().toString().trim();
        int jumlah_produk = Integer.parseInt(jumlah);
        String gambar_produk = "uploads/biru_putih.png";

        if (nama_produk.isEmpty()) {
            editTextNamaProduk.setError("Masa produk kamu gaada namanya?");
            editTextNamaProduk.requestFocus();
            return;
        }
        if (deskripsi_produk.isEmpty()) {
            editTextDeskripsi.setError("Isiin deskripsi produknya dong");
            editTextDeskripsi.requestFocus();
            return;
        }

        Call<TambahProduk> call = ApiClient
                .getInstance()
                .getApi()
                .tambahProduk(user_id, nama_produk, deskripsi_produk, kategori, sub_kategori_produk, toko_produk,
                        harga_produk, berat_produk, jumlah_produk, ketersediaan_produk, status_produk, gambar_produk);

        call.enqueue(new Callback<TambahProduk>() {
            @Override
            public void onResponse(Call<TambahProduk> call, Response<TambahProduk> response) {
                if (response.code() == 201) {

                    TambahProduk dr = response.body();
                    Toast.makeText(MitraTambahProdukActivity.this, dr.getMessage(), Toast.LENGTH_LONG).show();

                } else if (response.code() == 422) {
                    Toast.makeText(MitraTambahProdukActivity.this, "User already exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TambahProduk> call, Throwable t) {
                Toast.makeText(MitraTambahProdukActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.getData() != null){
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).fit().into(previewfoto);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tambahproduk:
                tambahProduk();

                startActivity(new Intent(this, MitraActivity.class));

            case R.id.btn_batal:

                startActivity(new Intent(this, MitraActivity.class));

                break;
        }
    }
}