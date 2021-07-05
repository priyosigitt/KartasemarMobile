package com.example.kartasemar.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.kartasemar.Adapter.HomeRekomendasiAdapter;
import com.example.kartasemar.Adapter.KategoriRajutanAdapter;
import com.example.kartasemar.GetAllProduk;
import com.example.kartasemar.Interface.ApiClient;
import com.example.kartasemar.MainActivity;
import com.example.kartasemar.Model.GetProduk;
import com.example.kartasemar.Model.Produk;
import com.example.kartasemar.activity.BatikActivity;
import com.example.kartasemar.activity.DaurUlangActivity;
import com.example.kartasemar.activity.KeranjangActivity;
import com.example.kartasemar.activity.ProdukDetailsActivity;
import com.example.kartasemar.R;
import com.example.kartasemar.activity.RajutActivity;
import com.example.kartasemar.activity.SearchActivity;
import com.example.kartasemar.fragments.ProdukDeskripsiFragment;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements HomeRekomendasiAdapter.OnItemClickListener{
    @Nullable
    ImageView detailproduk;

    Button btIns;
    ImageView keranjang,btnRajut,btnBatik,btnDaurUlang;
    ConstraintLayout search;
    private RecyclerView mRecyclerView;
    HomeRekomendasiAdapter adapter;
    List<Produk> produkList;
    private Context mCtx;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.home, container, false);

        keranjang = v.findViewById(R.id.btn_keranjang);
        keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), KeranjangActivity.class));
            }
        });

        search = v.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });

        btnRajut = v.findViewById(R.id.btn_rajut);
        btnRajut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RajutActivity.class));
            }
        });

        btnBatik = v.findViewById(R.id.btn_batik);
        btnBatik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BatikActivity.class));
            }
        });

        btnDaurUlang = v.findViewById(R.id.btn_daurulang);
        btnDaurUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DaurUlangActivity.class));
            }
        });

        this.mCtx = mCtx;
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewrekomendasi);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mCtx));
        refresh();
        return v;
    }

    public void refresh() {

        Call<GetProduk> call = ApiClient
                .getInstance()
                .getApi()
                .getProduk();

        call.enqueue(new Callback<GetProduk>() {
            @Override
            public void onResponse(Call<GetProduk> call, Response<GetProduk> response) {
                produkList = response.body().getData();

                adapter = new HomeRekomendasiAdapter(getActivity() ,produkList);
                adapter.setOnItemClickListener(HomeFragment.this::onItemClick);
                mRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<GetProduk> call, Throwable t) {

            }
        });

    }

    public void onItemClick(int position) {
        Produk produk = produkList.get(position);

        Intent detail = new Intent (getActivity(), ProdukDetailsActivity.class);

        detail.putExtra("produk_id", produk.getId_produk());
        detail.putExtra("produk_title", produk.getNama_produk());
        detail.putExtra("produk_deskripsi", produk.getDeskripsi_produk());
        detail.putExtra("produk_harga", produk.getHarga_produk());
        detail.putExtra("produk_gambar", produk.getGambar_produk());
        startActivity(detail);

    }

    private String formatRupiah(Double number){
        Locale localeID = new Locale("IND", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        String formatrupiah = numberFormat.format(number);
        String[] split = formatrupiah.split(",");
        int length = split[0].length();
        return split[0].substring(0,2)+". "+split[0].substring(2, length);
    }
}

