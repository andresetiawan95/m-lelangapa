package com.lelangapa.android.adapters.daftartawaranfinal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lelangapa.android.R;
import com.lelangapa.android.resources.BiddingResources;

import java.util.ArrayList;

/**
 * Created by andre on 11/05/17.
 */

public class UnchosenAdapter extends RecyclerView.Adapter<UnchosenAdapter.ViewHolder> {
    private Context context;
    private ArrayList<BiddingResources> listOffer;
    public UnchosenAdapter(Context context, ArrayList<BiddingResources> list) {
        this.context = context;
        this.listOffer = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_daftar_tawaran_final_items_layout_unchosen, parent, false);
        return new UnchosenAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BiddingResources resources = listOffer.get(position);
        holder.textView_namaBidder.setText(resources.getNamaBidder());
        holder.textView_tawaran.setText(resources.getHargaBid());
        holder.icon_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //implemented later
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOffer.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_namaBidder, textView_tawaran;
        private LinearLayout icon_choose;
        public ViewHolder (View view) {
            super(view);
            textView_namaBidder = (TextView) view.findViewById(R.id.fragment_daftar_tawaran_final_unchosen_name);
            textView_tawaran = (TextView) view.findViewById(R.id.fragment_daftar_tawaran_final_unchosen_offer);
            icon_choose = (LinearLayout) view.findViewById(R.id.fragment_daftar_tawaran_final_unchosen_choose);
        }
    }

}
