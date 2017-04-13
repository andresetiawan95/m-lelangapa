package com.lelangapa.android.adapters.category;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.android.R;

/**
 * Created by andre on 13/04/17.
 */

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {
    private Activity activity;
    private String[] listCategory;

    public HomeCategoryAdapter(Activity activity, String[] list)
    {
        this.activity = activity;
        this.listCategory = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView_namaKategori;
        public ImageView imageView_iconKategori;
        public ViewHolder(View view) {
            super(view);
            textView_namaKategori = (TextView) view.findViewById(R.id.fragment_beranda_home_category_items_nama);
            imageView_iconKategori = (ImageView) view.findViewById(R.id.fragment_beranda_home_category_items_icon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_beranda_home_category_layout_items, parent, false);
        return new HomeCategoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String namaKategori = listCategory[position];
        holder.textView_namaKategori.setText(namaKategori);
    }

    @Override
    public int getItemCount() {
        return listCategory.length;
    }
}
