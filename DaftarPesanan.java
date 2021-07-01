package com.example.kartasemar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;

import com.example.kartasemar.Adapter.AdapterPesanan;
import com.example.kartasemar.Adapter.AdapterProduk;
import com.example.kartasemar.Interface.ApiClient;
import com.example.kartasemar.Model.GetPesanan;
import com.example.kartasemar.Model.GetProduk;
import com.example.kartasemar.Model.Pesanan;
import com.example.kartasemar.Model.Produk;

import java.util.List;

public class DaftarPesanan extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    AdapterPesanan adapter;
    List<Pesanan> pesananList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pesanan);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewPesanan);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        refresh();
    }

    public void refresh() {

        Call<GetPesanan> call = ApiClient
                .getInstance()
                .getApi()
                .getPesanan();

        call.enqueue(new Callback<GetPesanan>() {
            @Override
            public void onResponse(Call<GetPesanan> call, Response<GetPesanan> response) {
                pesananList = response.body().getData();

                adapter = new AdapterPesanan(DaftarPesanan.this ,pesananList);
                mRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<GetPesanan> call, Throwable t) {

            }
        });

    }
}