package com.lelangapa.android.adapters.category;

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
 * Created by andre on 04/05/17.
 */

public class ItemCategoryAdapter extends RecyclerView.Adapter<ItemCategoryAdapter.ViewHolder>{
    private ArrayList<DetailItemResources> listItems;
    private Context context;

    public ItemCategoryAdapter(Context context, ArrayList<DetailItemResources> list) {
        this.context = context;
        this.listItems = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item_category_noempty_layout_items, parent, false);
        return new ItemCategoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DetailItemResources resources = listItems.get(position);
        if (resources.getUrlgambarbarang() != null)
        {
            Picasso.with(context).load(resources.getUrlgambarbarang()).into(holder.gambarbarang);
        }
        else {
            holder.gambarbarang.setImageResource(R.drawable.ic_insert_photo_grey_128dp);
        }
        holder.namabarang.setText(resources.getNamabarang());
        holder.user.setText(resources.getNamaauctioneer());
        holder.harga.setText(resources.getHargaawal());
        holder.kategori.setText(resources.getNamakategori());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView namabarang, user, harga, kategori;
        public ImageView gambarbarang;
        public ViewHolder (View view) {
            super(view);
            namabarang = (TextView) view.findViewById(R.id.fragment_item_category_layout_namabarang);
            user = (TextView) view.findViewById(R.id.fragment_item_category_layout_user);
            harga = (TextView) view.findViewById(R.id.fragment_item_category_layout_harga);
            kategori = (TextView) view.findViewById(R.id.fragment_item_category_layout_kategori);
            gambarbarang = (ImageView) view.findViewById(R.id.fragment_item_category_layout_imgview);
        }
    }
}
