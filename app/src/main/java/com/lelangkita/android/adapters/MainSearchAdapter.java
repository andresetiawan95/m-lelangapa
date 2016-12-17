package com.lelangkita.android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangkita.android.R;
import com.lelangkita.android.resources.SearchResultResources;

import java.util.ArrayList;

/**
 * Created by Andre on 12/17/2016.
 */

public class MainSearchAdapter extends RecyclerView.Adapter<MainSearchAdapter.MyViewHolder> {
    private ArrayList<SearchResultResources> searchResult;
    private Context context;
    public MainSearchAdapter(Context context, ArrayList<SearchResultResources> searchResult){
        this.searchResult = searchResult;
        this.context = context;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView namabarang, user, harga, status;
        public ImageView gambarbarang;
        public MyViewHolder (View view){
            super(view);
            namabarang = (TextView) view.findViewById(R.id.fragment_main_search_layout_namabarang);
            user = (TextView) view.findViewById(R.id.fragment_main_search_layout_user);
            harga = (TextView) view.findViewById(R.id.fragment_main_search_layout_harga);
            status = (TextView) view.findViewById(R.id.fragment_main_search_layout_status);
            gambarbarang = (ImageView) view.findViewById(R.id.fragment_main_search_layout_imgview);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_main_search_textsubmit_carditems, parent, false);
        return new MainSearchAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position)
    {
        SearchResultResources result = searchResult.get(position);
        viewHolder.namabarang.setText(result.getNamabarang());
        viewHolder.user.setText(result.getNamapengguna());
        viewHolder.harga.setText(result.getHargaawal());
        viewHolder.status.setText("active");
    }
    @Override
    public int getItemCount(){
        return searchResult.size();
    }
}