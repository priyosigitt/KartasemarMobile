package com.example.kartasemar.activity;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kartasemar.DaftarMitra;
import com.example.kartasemar.GetAllProduk;
import com.example.kartasemar.Interface.ApiClient;
import com.example.kartasemar.LoginMitra;
import com.example.kartasemar.Model.EditProduk;
import com.example.kartasemar.R;

public class MitraEditProdukActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editText;
    TextView textView;
    EditText textViewHargaproduk, textViewNamaProduk, textViewDeskripsiProduk, textViewJumlahProduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mitra_edit_produk);

        textViewHargaproduk = findViewById(R.id.editTextHarga);
        textViewNamaProduk = findViewById(R.id.editTextEditMitraNamaProduk);
        textViewDeskripsiProduk = findViewById(R.id.editTextDeskripsi);
        textViewJumlahProduk = findViewById(R.id.editTextJumlah);

        int produk_id = getIntent().getIntExtra("produk_id", 0);
        String produk_nama = getIntent().getStringExtra("produk_title");
        String produk_deskripsi = getIntent().getStringExtra("produk_deskripsi");
        int produk_harga = getIntent().getIntExtra("produk_harga", 0);
        int produk_jumlah = getIntent().getIntExtra("produk_jumlah", 0);

        textViewNamaProduk.setText(produk_nama);
        textViewDeskripsiProduk.setText(produk_deskripsi);
        textViewHargaproduk.setText("" + produk_harga);
        textViewJumlahProduk.setText("" + produk_jumlah);

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
                textView.setText(currentLength + "/100");
            }
        });
    }

    private void editProduk() {

        int id_produk = getIntent().getIntExtra("produk_id", 0);
        String nama_produk = textViewNamaProduk.getText().toString().trim();
        String deskripsi_produk = textViewDeskripsiProduk.getText().toString().trim();
        String harga = textViewHargaproduk.getText().toString().trim();
        int harga_produk = Integer.parseInt(harga);
        int berat_produk = 1000;
        String jumlah = textViewJumlahProduk.getText().toString().trim();
        int jumlah_produk = Integer.parseInt(jumlah);

        Call<EditProduk> call = ApiClient
                .getInstance()
                .getApi()
                .editProduk(id_produk, nama_produk, deskripsi_produk, harga_produk,
                        berat_produk, jumlah_produk);

        call.enqueue(new Callback<EditProduk>() {
            @Override
            public void onResponse(Call<EditProduk> call, Response<EditProduk> response) {
                if (response.code() == 201) {

                    EditProduk dr = response.body();
                    Toast.makeText(MitraEditProdukActivity.this, dr.getMessage(), Toast.LENGTH_LONG).show();

                } else if (response.code() == 422) {
                    Toast.makeText(MitraEditProdukActivity.this, "User already exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<EditProduk> call, Throwable t) {

                Toast.makeText(MitraEditProdukActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_updateproduk:
                //Toast.makeText(this, textViewNamaProduk.toString(), Toast.LENGTH_SHORT).show();
                editProduk();

                startActivity(new Intent(this, GetAllProduk.class));

            case R.id.btn_batal:

                startActivity(new Intent(this, GetAllProduk.class));

                break;
        }
    }
}
