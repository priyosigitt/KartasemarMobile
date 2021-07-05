package com.example.kartasemar.ui.ongkir;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kartasemar.R;
import com.example.kartasemar.data.cost.DataType;
import com.example.kartasemar.ui.search.SearchCityActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OngkirActivity extends AppCompatActivity implements OngkirContract.View {

    private static final int REQUEST_SOURCE = 1;
    private static final int REQUEST_DESTINATION = 2;

    private String source_id = "";
    private String destination_id = "";

    private OngkirPresenter presenter;
    private OngkirAdapter adapter;

    private List<DataType> data = new ArrayList<>();
    private List<String> courier = new ArrayList<>();

    TextInputEditText inputKotaAsal, inputKotaTujuan;
    TextView produk_namaongkir, produk_hargaongkir, produk_deskripsi, produk_gambar;
    Button btnSubmit;
    LinearLayout llMain;
    RecyclerView rvMain;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongkir);

        inputKotaAsal = findViewById(R.id.inputKotaAsal);
        inputKotaTujuan = findViewById(R.id.inputKotaTujuan);
        btnSubmit = findViewById(R.id.btnSubmit);
        llMain = findViewById(R.id.llMain);
        rvMain = findViewById(R.id.rvMain);
        progressBar = findViewById(R.id.progressBar);
        produk_namaongkir = findViewById(R.id.nama_produk_ongkir);
        produk_hargaongkir = findViewById(R.id.harga_produk_ongkir);

        int produk_id = getIntent().getIntExtra("produk_id",0);
        String produk_nama = getIntent().getStringExtra("produk_title");
        String produk_deskripsi = getIntent().getStringExtra("produk_deskripsi");
        int produk_harga = getIntent().getIntExtra("produk_harga", 0);
        String produk_gambar = getIntent().getStringExtra("produk_gambar");

        produk_namaongkir.setText(produk_nama);
        produk_hargaongkir.setText(formatRupiah((double)produk_harga));



        presenter = new OngkirPresenter(this);

        adapter = new OngkirAdapter(this, data, courier);
        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new LinearLayoutManager(this));


        inputKotaAsal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchCityActivity.class);
                intent.putExtra("requestCode", REQUEST_SOURCE);
                startActivityForResult(intent, REQUEST_SOURCE);
            }
        });

        inputKotaTujuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchCityActivity.class);
                intent.putExtra("requestCode", REQUEST_DESTINATION);
                startActivityForResult(intent, REQUEST_DESTINATION);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.clear();
                courier.clear();
                presenter.setupENV(getOrigin(), getDestination(), 1000);
            }
        });
    }

    @Override
    public void onLoadingCost(boolean loadng, int progress) {
        if (loadng) {
            llMain.setVisibility(View.VISIBLE);
            rvMain.setVisibility(View.GONE);
            progressBar.setProgress(progress);
        } else {
            progressBar.setProgress(progress);
            llMain.setVisibility(View.GONE);
            rvMain.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResultCost(List<DataType> data, List<String> courier) {
        this.data.addAll(data);
        this.courier.addAll(courier);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorCost() {
        rvMain.setVisibility(View.GONE);
        llMain.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getOrigin() {
        return source_id;
    }

    @Override
    public String getDestination() {
        return destination_id;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SOURCE && resultCode == RESULT_OK) {
            inputKotaAsal.setText(data.getStringExtra("city"));
            source_id = data.getStringExtra("city_id");
        } else if (requestCode == REQUEST_DESTINATION && resultCode == RESULT_OK) {
            inputKotaTujuan.setText(data.getStringExtra("city"));
            destination_id = data.getStringExtra("city_id");
        }
    }

    private String formatRupiah(double number){
        Locale localeID = new Locale("IND", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        String formatrupiah = numberFormat.format(number);
        String[] split = formatrupiah.split(",");
        int length = split[0].length();
        return split[0].substring(0,2)+". "+split[0].substring(2, length);
    }

}
