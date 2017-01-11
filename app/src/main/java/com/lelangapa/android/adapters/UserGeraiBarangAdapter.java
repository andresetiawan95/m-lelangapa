package com.lelangapa.android.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.UserGeraiResources;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by andre on 06/12/16.
 */

public class UserGeraiBarangAdapter extends RecyclerView.Adapter<UserGeraiBarangAdapter.MyViewHolder> {
    private ArrayList<UserGeraiResources> dataBarang;
    private Context context;
    public UserGeraiBarangAdapter (Context context, ArrayList<UserGeraiResources> dataBarang){
        this.dataBarang = dataBarang;
        this.context = context;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namabarang, user, harga, status;
        public ImageView gambarbarang;
        public CardView cardview;
        public MyViewHolder(View view){
            super(view);
            cardview = (CardView) view.findViewById(R.id.fragment_user_gerai_layout_cardview);
            gambarbarang = (ImageView) view.findViewById(R.id.fragment_user_gerai_layout_imgview);
            namabarang = (TextView) view.findViewById(R.id.fragment_user_gerai_layout_namabarang);
            user = (TextView) view.findViewById(R.id.fragment_user_gerai_layout_user);
            harga = (TextView) view.findViewById(R.id.fragment_user_gerai_layout_harga);
            status = (TextView) view.findViewById(R.id.fragment_user_gerai_layout_status);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user_gerai_layout_notempty_carditems, parent, false);
        return new UserGeraiBarangAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position){
        UserGeraiResources resBarang = dataBarang.get(position);
        if (resBarang.getUrlgambarbarang()!=null)
        {
            Picasso.with(context).load(resBarang.getUrlgambarbarang()).into(viewHolder.gambarbarang);
        }
        viewHolder.namabarang.setText(resBarang.getNamabarang());
        viewHolder.harga.setText(resBarang.getHargaawal());
        viewHolder.user.setText(resBarang.getNamapengguna());
        viewHolder.status.setText(resBarang.getStatuslelang());
    }
    @Override
    public int getItemCount(){
        return dataBarang.size();
    }
}
