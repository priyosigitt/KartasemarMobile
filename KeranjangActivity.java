package com.example.kartasemar.activity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.kartasemar.Adapter.KeranjangAdapter;
import com.example.kartasemar.Interface.ApiClient;
import com.example.kartasemar.Model.GetKeranjangCustomer;
import com.example.kartasemar.Model.Keranjang;
import com.example.kartasemar.R;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KeranjangActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    KeranjangAdapter adapter;
    List<Keranjang> keranjangList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewkeranjang);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        refresh();
    }

    private void refresh() {

        int id_customer = 400;

        Call<GetKeranjangCustomer> call = ApiClient
                .getInstance()
                .getApi()
                .getKeranjangCustomer(id_customer);

        call.enqueue(new Callback<GetKeranjangCustomer>() {
            @Override
            public void onResponse(Call<GetKeranjangCustomer> call, Response<GetKeranjangCustomer> response) {
                keranjangList = response.body().getData();
                if (response.code() == 200) {
                    adapter = new KeranjangAdapter(KeranjangActivity.this , keranjangList);

                    mRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(KeranjangActivity.this, "Kosong!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetKeranjangCustomer> call, Throwable t) {

            }
        });

    }


}
