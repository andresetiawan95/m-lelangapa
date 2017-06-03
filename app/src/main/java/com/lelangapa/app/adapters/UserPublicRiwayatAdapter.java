package com.lelangapa.app.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.resources.RiwayatResources;

import java.util.ArrayList;

/**
 * Created by andre on 11/03/17.
 */

public class UserPublicRiwayatAdapter extends RecyclerView.Adapter<UserPublicRiwayatAdapter.RiwayatViewHolder>{
    private Context context;
    private ArrayList<RiwayatResources> listRiwayat;
    public UserPublicRiwayatAdapter(Context context, ArrayList<RiwayatResources> listRiwayat)
    {
        this.context = context;
        this.listRiwayat = listRiwayat;
    }

    public class RiwayatViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_judulItem, textView_namaAuctioneer,
                textView_kalibid, textView_hargabid, textView_statuswin;
        public ImageView imageView_imageItem;
        public CardView cardView;
        public RiwayatViewHolder(View view)
        {
            super(view);
            cardView = (CardView) view.findViewById(R.id.fragment_detail_user_public_riwayat_noempty_cardview);
            textView_judulItem = (TextView) view.findViewById(R.id.fragment_detail_user_public_riwayat_noempty_judulitem);
            textView_namaAuctioneer = (TextView) view.findViewById(R.id.fragment_detail_user_public_riwayat_noempty_namaauctioneer);
            textView_kalibid = (TextView) view.findViewById(R.id.fragment_detail_user_public_riwayat_noempty_bidkali);
            textView_hargabid = (TextView) view.findViewById(R.id.fragment_detail_user_public_riwayat_noempty_hargabid);
            textView_statuswin = (TextView) view.findViewById(R.id.fragment_detail_user_public_riwayat_noempty_statuswin);
            imageView_imageItem = (ImageView) view.findViewById(R.id.fragment_detail_user_public_riwayat_noempty_image);
        }
    }

    @Override
    public RiwayatViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_detail_user_public_riwayat_noempty_layout_items, parent, false);
        return new UserPublicRiwayatAdapter.RiwayatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RiwayatViewHolder viewHolder, int position)
    {
        RiwayatResources riwayatResources = listRiwayat.get(position);
        viewHolder.textView_judulItem.setText(riwayatResources.getNamaItem());
        viewHolder.textView_namaAuctioneer.setText(riwayatResources.getNamaAuctioneer());
        viewHolder.textView_kalibid.setText(Integer.toString(riwayatResources.getBidTime()));
        viewHolder.textView_hargabid.setText(riwayatResources.getHargaBid());
        if (riwayatResources.getWinStatus() && riwayatResources.getBidStatus() == -1)
        {
            viewHolder.textView_statuswin.setText("MENANG");
            viewHolder.textView_statuswin.setBackgroundResource(R.color.riwayatWin);
        }
        else if (riwayatResources.getBidStatus() != -1)
        {
            viewHolder.textView_statuswin.setText("BERLANGSUNG");
            viewHolder.textView_statuswin.setBackgroundResource(R.color.riwayatBerlangsung);
        }
        else
        {
            viewHolder.textView_statuswin.setText("KALAH");
            viewHolder.textView_statuswin.setBackgroundResource(R.color.riwayatStatusLose);
        }
    }

    public void updateDataset(ArrayList<RiwayatResources> listRiwayat)
    {
        this.listRiwayat = listRiwayat;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return listRiwayat.size();
    }
}
