package com.lelangapa.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelangapa.app.R;
import com.lelangapa.app.resources.DateTimeConverter;
import com.lelangapa.app.resources.PriceFormatter;
import com.lelangapa.app.resources.RiwayatResources;

import java.util.ArrayList;

/**
 * Created by andre on 16/03/17.
 */

public class UserRiwayatDetailAdapter extends RecyclerView.Adapter<UserRiwayatDetailAdapter.RiwayatDetailViewHolder> {
    private Context context;
    private ArrayList<RiwayatResources> biddingList;
    private DateTimeConverter dateTimeConverter;

    public UserRiwayatDetailAdapter(Context context, ArrayList<RiwayatResources> list)
    {
        this.context = context;
        this.biddingList = list;
        this.dateTimeConverter = new DateTimeConverter();
    }
    public class RiwayatDetailViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_bidTimestamp, textView_hargaBid;
        public RiwayatDetailViewHolder(View view)
        {
            super(view);
            textView_bidTimestamp = (TextView) view.findViewById(R.id.fragment_user_riwayat_detail_noempty_tanggal);
            textView_hargaBid = (TextView) view.findViewById(R.id.fragment_user_riwayat_detail_noempty_tawaran);
        }
    }

    @Override
    public RiwayatDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user_riwayat_detail_noempty_layout_list, parent, false);
        return new UserRiwayatDetailAdapter.RiwayatDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RiwayatDetailViewHolder holder, int position) {
        RiwayatResources riwayatResources = biddingList.get(position);
        String indonesiaTimestampFormat = dateTimeConverter.convertUTCToLocalTimeIndonesiaFormat(riwayatResources.getBidTimestamp());
        holder.textView_bidTimestamp.setText(indonesiaTimestampFormat);
        holder.textView_hargaBid.setText(PriceFormatter.formatPrice(riwayatResources.getHargaBid()));
    }

    @Override
    public int getItemCount() {
        return biddingList.size();
    }
}
