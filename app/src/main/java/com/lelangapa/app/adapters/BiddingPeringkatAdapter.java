package com.lelangapa.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.resources.BiddingResources;

import java.util.ArrayList;

/**
 * Created by andre on 09/01/17.
 */

public class BiddingPeringkatAdapter extends RecyclerView.Adapter<BiddingPeringkatAdapter.PeringkatViewHolder> {
    private ArrayList<BiddingResources> biddingPeringkatList;
    private Context context;
    public BiddingPeringkatAdapter(Context context, ArrayList<BiddingResources> biddingPeringkatList)
    {
        this.context = context;
        this.biddingPeringkatList = biddingPeringkatList;
    }
    public class PeringkatViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView_avatar;
        public TextView textView_biddernama, textView_bidderprice;
        public PeringkatViewHolder (View view)
        {
            super(view);
            imageView_avatar = (ImageView) view.findViewById(R.id.fragment_detail_barang_peringkat_top_three_snippet_avatar);
            textView_biddernama = (TextView) view.findViewById(R.id.fragment_detail_barang_peringkat_top_three_snippet_name);
            textView_bidderprice = (TextView) view.findViewById(R.id.fragment_detail_barang_peringkat_top_three_snippet_bidprice);
        }
    }
    @Override
    public PeringkatViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_detail_barang_peringkat_top_three_snippet_layout, parent, false);
        return new BiddingPeringkatAdapter.PeringkatViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(PeringkatViewHolder viewHolder, int position)
    {
        BiddingResources singlePeringkatItem = biddingPeringkatList.get(position);
        //karena profile picture belum diterapkan, maka imageView belum di set dulu.
        //yang di set adalah nama dan price bidd dulu
        viewHolder.textView_biddernama.setText(singlePeringkatItem.getNamaBidder());
        viewHolder.textView_bidderprice.setText(singlePeringkatItem.getHargaBid());
    }
    @Override
    public int getItemCount()
    {
        if (biddingPeringkatList.size() >= 3) return 3;
        return biddingPeringkatList.size();
    }
    public void updateDataSet(ArrayList<BiddingResources> biddingPeringkatList)
    {
        //menggunakan notifyDataSetChanged() untuk memberitahu adapter bahwa ada perubahan pada data set
        this.biddingPeringkatList = biddingPeringkatList;
        notifyDataSetChanged();
    }
}
