package com.example.kartasemar.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.kartasemar.Adapter.MitraProdukAdapter;
import com.example.kartasemar.Model.ProdukMitra;
import com.example.kartasemar.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MitraGetProdukActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    FloatingActionButton btnedit;
    MitraProdukAdapter adapter;
    List<ProdukMitra> produkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mitra_get_produk);
    }
}