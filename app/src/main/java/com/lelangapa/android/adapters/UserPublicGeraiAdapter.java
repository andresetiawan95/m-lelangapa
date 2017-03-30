package com.lelangapa.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.DetailItemResources;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by andre on 29/03/17.
 */

public class UserPublicGeraiAdapter extends RecyclerView.Adapter<UserPublicGeraiAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DetailItemResources> listGeraiItem;

    public UserPublicGeraiAdapter(Context context, ArrayList<DetailItemResources> list)
    {
        this.context = context;
        this.listGeraiItem = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView_gambarBarang;
        public TextView textView_namaBarang, textView_namaAuctioneer, textView_hargaAwal;
        public ViewHolder (View view)
        {
            super(view);
            imageView_gambarBarang = (ImageView) view.findViewById(R.id.fragment_detail_user_public_gerai_layout_imgview);
            textView_namaBarang = (TextView) view.findViewById(R.id.fragment_detail_user_public_gerai_layout_namabarang);
            textView_namaAuctioneer = (TextView) view.findViewById(R.id.fragment_detail_user_public_gerai_layout_user);
            textView_hargaAwal = (TextView) view.findViewById(R.id.fragment_detail_user_public_gerai_layout_harga);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_detail_user_public_gerai_noempty_layout_items, parent, false);
        return new UserPublicGeraiAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DetailItemResources detailItem = listGeraiItem.get(position);
        if (detailItem.getUrlgambarbarang() != null) {
            Picasso.with(context).load(detailItem.getUrlgambarbarang()).into(holder.imageView_gambarBarang);
        }
        else {
            holder.imageView_gambarBarang.setImageResource(R.drawable.ic_insert_photo_grey_128dp);
        }
        holder.textView_namaBarang.setText(detailItem.getNamabarang());
        holder.textView_namaAuctioneer.setText(detailItem.getNamaauctioneer());
        holder.textView_hargaAwal.setText(detailItem.getHargaawal());
    }

    @Override
    public int getItemCount() {
        return listGeraiItem.size();
    }

    public void updateDataset(ArrayList<DetailItemResources> list)
    {
        this.listGeraiItem = list;
        notifyDataSetChanged();
    }
}
